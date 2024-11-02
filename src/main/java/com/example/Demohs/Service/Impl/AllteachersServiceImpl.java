package com.example.Demohs.Service.Impl;

import com.example.Demohs.Dto.AllTeachersDto;
import com.example.Demohs.Entity.AllTeachers;
import com.example.Demohs.Exception.ResourceAlreadyExistsException;
import com.example.Demohs.Exception.ResourceNotFoundException;
import com.example.Demohs.Repository.AllTeachersRepository;
import com.example.Demohs.Service.AllTeachersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AllteachersServiceImpl implements AllTeachersService {


    @Autowired
    private AllTeachersRepository allTeachersRepository;

    @Override
    public String addTeacher(AllTeachersDto allTeachersDto) {

        Optional<AllTeachers> allTeachersOptional = allTeachersRepository.findByRegNo(allTeachersDto.getRegNo());

        if(allTeachersOptional.isPresent())
        {
            throw new ResourceAlreadyExistsException("already exists with this registration Number");
        }

        AllTeachersDto allTeachersDto1 = new AllTeachersDto();
        AllTeachers allTeachers =  allTeachersDto1.toAllTeachers(allTeachersDto);

        AllTeachers allTeachers1 = allTeachersRepository.save(allTeachers);

        return "Registration number "+allTeachers1.getRegNo()+" is successfully added.";
    }

    @Override
    public String deleteTeacher(String regNo) {

        Optional<AllTeachers> allTeachersOptional = allTeachersRepository.findByRegNo(regNo);

        if(allTeachersOptional.isEmpty())
        {
            throw new ResourceNotFoundException("Faculty is not available with this registration Number");
        }

        allTeachersRepository.deleteById(allTeachersOptional.get().getTeacherId());

        return "Deleted Successfully with the registration number "+allTeachersOptional.get().getRegNo();

    }

    @Override
    public String updateTeacher(AllTeachersDto allTeachersDto) {

        return null;
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
    public List<AllTeachersDto> getAllTeachers() {
        List<AllTeachers> allTeachersOptional = allTeachersRepository.findAll();

        if(allTeachersOptional.isEmpty())
        {
            throw new ResourceNotFoundException("no Teachers are found ");
        }

        List<AllTeachersDto> allTeachersDtoList = new ArrayList<>();

        for (AllTeachers teachers : allTeachersOptional) {

            AllTeachers allTeachers = new AllTeachers();
            allTeachersDtoList.add(allTeachers.toAllTeachersDto(teachers));

        }
        return allTeachersDtoList;

    }
}
