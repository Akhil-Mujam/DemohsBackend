package com.example.Demohs.Controller;

import com.example.Demohs.Dto.ExamTypeDto;
import com.example.Demohs.Entity.ExamType;
import com.example.Demohs.Service.ExamTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/examtype")
public class ExamTypeController {
    @Autowired
    ExamTypeService examTypeService;

    @PostMapping("/{examType}")
    public ResponseEntity<ExamTypeDto> createExamType(@PathVariable String examType)
    {
        ExamTypeDto examTypeDto=examTypeService.createExamType(examType);
        return new ResponseEntity<>(examTypeDto, HttpStatus.CREATED);
    }
}
