package com.example.Demohs.Service.Impl;

import com.example.Demohs.Dto.AllTeachersDto;
import com.example.Demohs.Entity.AllTeachers;
import com.example.Demohs.Entity.TeacherClass;
import com.example.Demohs.Exception.ResourceAlreadyExistsException;
import com.example.Demohs.Exception.ResourceNotFoundException;
import com.example.Demohs.Repository.AllTeachersRepository;
import com.example.Demohs.Repository.ClassTeacherRepository;
import com.example.Demohs.Service.AllTeachersService;
import com.example.Demohs.Service.ClassTeacherService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClassTeacherImpl implements ClassTeacherService {
   @Autowired
    private  ClassTeacherRepository classTeacherRepository;

   @Autowired
   private AllTeachersRepository allTeachersRepository;

   @Autowired
   private AllTeachersService allTeachersService;

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
        teacherClass.setTeacher(allTeachersOptional.get());
        teacherClass.setClassSection(classSection);
        classTeacherRepository.save(teacherClass);


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
}
