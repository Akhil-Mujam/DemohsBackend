package com.example.Demohs.Service.Impl;

import com.example.Demohs.Dto.AllTeachersDto;
import com.example.Demohs.Dto.UserAuthDataDto;
import com.example.Demohs.Entity.AllTeachers;
import com.example.Demohs.Entity.StudentMaster;
import com.example.Demohs.Entity.UserAuthData;
import com.example.Demohs.Exception.ResourceAlreadyExistsException;
import com.example.Demohs.Exception.ResourceNotFoundException;
import com.example.Demohs.Repository.AllTeachersRepository;
import com.example.Demohs.Repository.UserAuthDataRepository;
import com.example.Demohs.Service.AllTeachersService;
import com.example.Demohs.Service.UserAuthDataService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AllteachersServiceImpl implements AllTeachersService {


    @Autowired
    private AllTeachersRepository allTeachersRepository;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserAuthDataService userAuthDataService;

    @Autowired
    UserAuthDataRepository userAuthDataRepository;


    @Override
    public String addTeacher(AllTeachersDto allTeachersDto) {

        Optional<AllTeachers> allTeachersOptional = allTeachersRepository.findByRegNo(allTeachersDto.getRegNo());

        if(allTeachersOptional.isPresent())
        {
            throw new ResourceAlreadyExistsException("already exists with this registration Number");
        }

        // Capitalize firstName, lastName, and address
        allTeachersDto.setFirstName(capitalize(allTeachersDto.getFirstName()));
        allTeachersDto.setLastName(capitalize(allTeachersDto.getLastName()));
        allTeachersDto.setAddress(capitalize(allTeachersDto.getAddress()));

        UserAuthDataDto userAuthDataDto=new UserAuthDataDto();
        userAuthDataDto.setUserName(allTeachersDto.getRegNo());
        userAuthDataDto.setPassword(allTeachersDto.getPassword());
        userAuthDataDto.setRole(allTeachersDto.getRole());
        userAuthDataService.create(userAuthDataDto);

        AllTeachers allTeachers=new AllTeachers();
        allTeachers=modelMapper.map(allTeachersDto,AllTeachers.class);
        allTeachers.setTeacherId(UUID.randomUUID());
        AllTeachers allTeachers1 = allTeachersRepository.save(allTeachers);

        return "Registration number "+allTeachers1.getRegNo()+" is successfully added.";
    }

    private String capitalize(String input) {
        if (input == null || input.trim().isEmpty()) {
            return input;
        }
        return Arrays.stream(input.split("\\s+"))  // Split words by spaces
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()) // Capitalize first letter
                .collect(Collectors.joining(" "));  // Join words back into a sentence
    }


    @Override
    @Transactional
    public String deleteTeacher(String regNo) {
        // Check if the teacher exists in AllTeachers
        Optional<AllTeachers> allTeachersOptional = allTeachersRepository.findByRegNo(regNo);

        if (allTeachersOptional.isEmpty()) {
            throw new ResourceNotFoundException("Faculty is not available with this registration Number");
        }

        // Fetch the teacher entity
        AllTeachers teacher = allTeachersOptional.get();

        // Delete the teacher record from AllTeachers
        allTeachersRepository.deleteById(teacher.getTeacherId());

        // Check if the teacher exists in UserAuthData
        Optional<UserAuthData> userAuthDataOptional = userAuthDataRepository.findByUsername(regNo);

        if (userAuthDataOptional.isPresent()) {
            // Delete the corresponding UserAuthData record
            userAuthDataRepository.delete(userAuthDataOptional.get());
        }

        return "Deleted successfully with the registration number " + regNo;
    }


    @Override
    public String updateTeacher(AllTeachersDto allTeachersDto) {
        AllTeachers allTeachers = allTeachersRepository.findById(allTeachersDto.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher Not Found"));

        String username = allTeachers.getRegNo();

        // Capitalize necessary fields before updating
        allTeachersDto.setFirstName(capitalize(allTeachersDto.getFirstName()));
        allTeachersDto.setLastName(capitalize(allTeachersDto.getLastName()));
        allTeachersDto.setAddress(capitalize(allTeachersDto.getAddress()));

        modelMapper.map(allTeachersDto, allTeachers);
        if (username != null) {
            System.out.println("allteachers.getRegNo() =  " + username);
            UserAuthData userAuthData = userAuthDataService.findByUserName(username);
            System.out.println("userauth =" + userAuthData.getUsername());

            if (userAuthData.getUsername() == null) {
                throw new ResourceNotFoundException("User Not found");
            } else {

                userAuthData.setUsername(allTeachers.getRegNo());
                userAuthData.setPassword(allTeachers.getPassword());
                userAuthData.setUserRole(allTeachers.getRole());

                allTeachersRepository.save(allTeachers);
                userAuthDataRepository.save(userAuthData);
            }


        }
        return "successfully updated";
    }

    @Override
    public AllTeachers getTeacher(String regNo) {

        Optional<AllTeachers> allTeachersOptional = allTeachersRepository.findByRegNo(regNo);

        if(allTeachersOptional.isEmpty())
        {
            throw new ResourceNotFoundException("resource not found with this given regno: "+regNo);
        }

        return allTeachersOptional.get();
    }



    @Override
    public Page<AllTeachersDto> getAllTeachers(int page, int size) {
        // Create a pageable object with page number and size
        Pageable pageable = PageRequest.of(page, size);

        // Fetch paginated data from the repository
        Page<AllTeachers> allTeachersPage = allTeachersRepository.findAll(pageable);

//        if (allTeachersPage.isEmpty()) {
//            throw new ResourceNotFoundException("No teachers are found");
//        }

        // Map the paginated entities to DTOs

        return allTeachersPage.map(teacher -> modelMapper.map(teacher, AllTeachersDto.class));
    }

}
