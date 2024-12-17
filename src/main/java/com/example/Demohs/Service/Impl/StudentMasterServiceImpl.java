package com.example.Demohs.Service.Impl;

import com.example.Demohs.Dto.StudentMasterDto;
import com.example.Demohs.Dto.UserAuthDataDto;
import com.example.Demohs.Entity.StudentMaster;
import com.example.Demohs.Entity.UserAuthData;
import com.example.Demohs.Exception.ResourceAlreadyExistsException;
import com.example.Demohs.Exception.ResourceNotFoundException;
import com.example.Demohs.Repository.StudentMasterRepository;
import com.example.Demohs.Repository.UserAuthDataRepository;
import com.example.Demohs.Service.StudentMasterService;
import com.example.Demohs.Service.UserAuthDataService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentMasterServiceImpl implements StudentMasterService {

    @Autowired
    private StudentMasterRepository studentMasterRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserAuthDataService userAuthDataService;

    @Autowired
    UserAuthDataRepository userAuthDataRepository;

    @Override
    public String addStudent(StudentMasterDto studentMasterDto) {

         Optional<StudentMaster> studentMasterOptional = studentMasterRepository.findByRegNo(studentMasterDto.getRegNo());

         if(studentMasterOptional.isPresent())
         {
             throw new ResourceAlreadyExistsException("already exists");
         }
         UserAuthDataDto userAuthDataDto=new UserAuthDataDto();
         userAuthDataDto.setUserName(studentMasterDto.getRegNo());
         userAuthDataDto.setPassword(studentMasterDto.getPassword());
         userAuthDataDto.setRole(studentMasterDto.getRole());
         userAuthDataService.create(userAuthDataDto);
         StudentMaster studentMaster =modelMapper.map(studentMasterDto, StudentMaster.class);
         studentMaster.setStudentId(UUID.randomUUID());
         studentMasterRepository.save(studentMaster);
         return "Student Created";
    }

    @Override
    public String deleteStudent(String regNo) {
        Optional<StudentMaster> studentMasterOptional = studentMasterRepository.findByRegNo(regNo);

        if(studentMasterOptional.isEmpty())
        {
            throw new ResourceNotFoundException("not exists with this regNo:"+regNo+" to Delete");
        }

        studentMasterRepository.delete(studentMasterOptional.get());

        return "Deleted successfully";
    }

    @Override
    public StudentMasterDto getStudentByRegNo(String regNo) {
        Optional<StudentMaster> studentMasterOptional = studentMasterRepository.findByRegNo(regNo);

        if(studentMasterOptional.isEmpty())
        {
            throw new ResourceNotFoundException("not exists with this regNo: "+regNo);
        }
        StudentMaster studentMaster = new StudentMaster();

        return modelMapper.map(studentMaster,StudentMasterDto.class);
    }

    @Override
    public List<StudentMasterDto> getAllStudents() {
        List<StudentMaster> studentMasters = studentMasterRepository.findAll();

        List<StudentMasterDto> studentMasterDtoList = new ArrayList<>();
        for (StudentMaster studentMaster : studentMasters) {
            studentMasterDtoList.add(modelMapper.map(studentMaster,StudentMasterDto.class));
        }

        return studentMasterDtoList;
    }

    @Override
    public String updateStudentById(StudentMasterDto studentMasterDto) {

        // Fetch the existing student record
        StudentMaster studentMaster = studentMasterRepository.findById(studentMasterDto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentMasterDto.getStudentId()));

        String username = studentMaster.getRegNo();
        studentMaster.setFirstName(studentMasterDto.getFirstName());
        studentMaster.setLastName(studentMasterDto.getLastName());
        studentMaster.setAddress(studentMasterDto.getAddress());
        studentMaster.setPhno(studentMasterDto.getPhno());
        studentMaster.setMotherName(studentMasterDto.getMotherName());
        studentMaster.setFatherName(studentMasterDto.getFatherName());
        studentMaster.setRegNo(studentMasterDto.getRegNo());
        studentMaster.setClassesEntity(studentMasterDto.getClassesEntity());
        studentMaster.setClassSection(studentMasterDto.getClassSection());

        if(username!=null)
        {
            System.out.println("studentMaster.getRegNo() =  "+username);
            UserAuthData userAuthData = userAuthDataService.findByUserName(username);
            System.out.println("userauth =" + userAuthData.getUsername());

            if (userAuthData.getUsername()==null) {
                throw new ResourceNotFoundException("User Not found");
            }
          else{

              userAuthData.setUsername(studentMasterDto.getRegNo());
              userAuthData.setPassword(studentMasterDto.getPassword());
              userAuthData.setUserRole(studentMasterDto.getRole());

              studentMasterRepository.save(studentMaster);
              userAuthDataRepository.save(userAuthData);
          }

        }

        return "Updated successfully";
    }


    @Override
    public StudentMaster findByRegNo(String regNo) {

        Optional<StudentMaster> studentMasterOptional=studentMasterRepository.findByRegNo(regNo);
        if(studentMasterOptional.isEmpty())
        {
            throw new ResourceNotFoundException("Student Not Found"+regNo);
        }
        return studentMasterOptional.get();
    }

//    @Override
//    public List<StudentMasterDto> findByClassesEntityAndClassSection(String classesEntity, String classSection) {
//        // Fetch the list of StudentMaster entities
//        List<StudentMaster> studentMasterList = studentMasterRepository.findByClassesEntityAndClassSection(classesEntity, classSection);
//
//        // Check for null or empty list and return an empty list if needed
//        if (studentMasterList == null || studentMasterList.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        // Use Java Streams to map entities to DTOs
//        return studentMasterList.stream()
//                .map(student -> modelMapper.map(student, StudentMasterDto.class))
//                .collect(Collectors.toList());
//    }

    @Override
    public List<StudentMaster> findByClassesEntityAndClassSection(String classesEntity, String classSection) {
        return studentMasterRepository.findByClassesEntityAndClassSection(classesEntity,classSection);
    }

    @Override
    public List<StudentMaster> findByClassesEntityAndClassSectionPagination(String classesEntity, String classSection, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<StudentMaster> studentPage = studentMasterRepository.findByClassesEntityAndClassSection(classesEntity, classSection, pageable);
        return studentPage.getContent();
    }


    @Override
    public Page<StudentMasterDto> getByClassNameAndSection(String classId, String classSection, int page, int size) {
        return null;
    }



//    @Override
//    public Page<StudentMasterDto> getByClassNameAndSection(String classId, String classSection, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<StudentMaster> studentMastersPage = studentMasterRepository.findByClassesEntityAndClassSection(classId, classSection, pageable);
//
//        return studentMastersPage.map(student -> modelMapper.map(student,StudentMasterDto.class));
//    }

}
