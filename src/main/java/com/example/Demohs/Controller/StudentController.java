package com.example.Demohs.Controller;

import com.example.Demohs.Dto.StudentDto;
import com.example.Demohs.Service.StudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {

    @Autowired
    public StudentService studentService;

    @PostMapping("/")
    public ResponseEntity<String> StudentRegister(@Valid @RequestBody StudentDto studentDto)
    {

        String s =  studentService.StudentRegister(studentDto);

        return new ResponseEntity<>(s, HttpStatus.CREATED);
    }



}