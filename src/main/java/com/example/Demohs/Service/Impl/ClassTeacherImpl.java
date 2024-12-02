package com.example.Demohs.Service.Impl;

import com.example.Demohs.Dto.AllTeachersDto;
import com.example.Demohs.Dto.UserAuthDataDto;
import com.example.Demohs.Entity.AllTeachers;
import com.example.Demohs.Entity.TeacherClass;
import com.example.Demohs.Entity.UserAuthData;
import com.example.Demohs.Exception.ResourceAlreadyExistsException;
import com.example.Demohs.Exception.ResourceNotFoundException;
import com.example.Demohs.Repository.AllTeachersRepository;
import com.example.Demohs.Repository.ClassTeacherRepository;
import com.example.Demohs.Service.AllTeachersService;
import com.example.Demohs.Service.ClassTeacherService;
import com.example.Demohs.Service.UserAuthDataService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClassTeacherImpl implements ClassTeacherService {
   @Autowired
    private  ClassTeacherRepository classTeacherRepository;

   @Autowired
   private AllTeachersRepository allTeachersRepository;

   @Autowired
   private AllTeachersService allTeachersService;

   @Autowired
   private UserAuthDataService userAuthDataService;

   @Autowired
    ModelMapper modelMapper;
    @Override
    public String makeClassTeacher(String regNo,String ClassName,String classSection) {

        Optional<AllTeachers> allTeachersOptional = allTeachersRepository.findByRegNo(regNo);


        if(allTeachersOptional.isEmpty())
        {
            throw  new ResourceNotFoundException("resource not found with regno: "+regNo);

        }
        if(classTeacherRepository.existsByTeacher_RegNo(regNo))
        {
            throw new ResourceAlreadyExistsException("already exists as class Teacher..");
        }
        TeacherClass teacherClass = new TeacherClass();
        teacherClass.setClassEntity(ClassName);
        teacherClass.setId(UUID.randomUUID());
        teacherClass.setTeacher(allTeachersOptional.get());
        teacherClass.setClassSection(classSection);
        classTeacherRepository.save(teacherClass);

        //adding classTeacher to AllTeachers
        AllTeachers allTeachers= allTeachersOptional.get();
        allTeachers.setTeacherClass(teacherClass);
        allTeachers.setTeacherId(allTeachers.getTeacherId());
        allTeachersRepository.save(allTeachers);


        UserAuthData userAuthData=userAuthDataService.findByUserName(regNo);
        String userRole=userAuthData.getStringUserRole();
        userRole+=",ClassTeacher";
        userAuthData.setUserRole(userRole);
        userAuthData.setUserID(userAuthData.getUserID());
        userAuthDataService.update(userAuthData);



        return "add Teacher for the class:"+ ClassName;
    }

    @Override
    public AllTeachersDto getTeacherByClassName(String ClassName) {

        Optional<TeacherClass> allTeachersOptional = classTeacherRepository.findByClassEntity(ClassName);

        if(allTeachersOptional.isEmpty())
        {
            throw new ResourceNotFoundException("className is not found");
        }

        AllTeachersDto allTeachersDto = modelMapper.map(allTeachersOptional.get(),AllTeachersDto.class);
        return null;
    }

    @Override
    public TeacherClass getClassTeacher(String regNo) {
        AllTeachers allTeachers=allTeachersService.getTeacher(regNo);
        return classTeacherRepository.findByTeacher(allTeachers);
    }

    @Override
    public Map<String, String> getClassDetailsByRegNo(String regNo) {
        Optional<TeacherClass> teacherClassOptional = classTeacherRepository.findTeacherClassByRegNo(regNo);

        if (teacherClassOptional.isPresent()) {
            TeacherClass teacherClass = teacherClassOptional.get();
            Map<String, String> classDetails = new HashMap<>();
            classDetails.put("classEntity", teacherClass.getClassEntity());
            classDetails.put("classSection", teacherClass.getClassSection());
            return classDetails;
        } else {
            throw new ResourceNotFoundException("The teacher with regNo " + regNo + " is not a class teacher.");
        }
    }
}
