package com.example.Demohs.Controller;

import com.example.Demohs.Dto.SubjectDto;
import com.example.Demohs.Service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/subject")
public class SubjectController {
    @Autowired
    SubjectService subjectService;

    @PostMapping("/{subjectName}")
    public ResponseEntity<SubjectDto> createSubject(@PathVariable String subjectName)
    {
        SubjectDto subjectDto=subjectService.createSubject(subjectName);
        return new ResponseEntity<>(subjectDto, HttpStatus.CREATED);
    }
}
