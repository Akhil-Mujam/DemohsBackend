package com.example.Demohs.Controller;

import com.example.Demohs.Dto.SubjectDto;
import com.example.Demohs.Service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
