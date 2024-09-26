package com.example.Demohs.Dto;

import com.example.Demohs.Entity.Student;
import jakarta.validation.constraints.NotEmpty;


public class StudentDto {

    public Long Id;

    @NotEmpty(message = "email should not be empty")
    public String email;

    @NotEmpty(message = "password should not be empty")
    public String password;

    public Student toStudent(StudentDto studentDto)
    {
        Student stu = new Student();
        stu.email=studentDto.email;
        stu.password=studentDto.password;
        stu.Id= studentDto.Id;
        return stu;
    }
}
