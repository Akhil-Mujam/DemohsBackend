package com.example.Demohs.Controller;

import com.example.Demohs.Dto.ExamTypeDto;
import com.example.Demohs.Entity.ExamType;
import com.example.Demohs.Service.ExamTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
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
//    http://localhost:9090/examtype/FA1
//    http://localhost:9090/examtype/FA2
//    http://localhost:9090/examtype/SA1
//    http://localhost:9090/examtype/FA3
//    http://localhost:9090/examtype/FA4
//    http://localhost:9090/examtype/SA2
}
