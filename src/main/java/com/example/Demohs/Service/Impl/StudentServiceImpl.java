package com.example.Demohs.Service.Impl;

import com.example.Demohs.Dto.StudentDto;
import com.example.Demohs.Entity.Student;
import com.example.Demohs.Repository.StudentRepository;
import com.example.Demohs.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    public StudentRepository studentRepository;



    @Override
    public String StudentRegister(StudentDto studentDto) {

        StudentDto obj1= new StudentDto();
        System.out.println(studentDto.email);
        Student s  = obj1.toStudent(studentDto);
        System.out.println(s.toString());
        studentRepository.save(s);
        return "Registered Successfully";
    }
}
