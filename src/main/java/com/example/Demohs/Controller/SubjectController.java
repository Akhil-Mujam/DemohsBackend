package com.example.Demohs.Controller;

import com.example.Demohs.Dto.SubjectDto;
import com.example.Demohs.Service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/all")
    public ResponseEntity<List<SubjectDto>> getAllSubjects()
    {
        List<SubjectDto> subjectDtoList = subjectService.getAllSubjects();
        return  new ResponseEntity<>(subjectDtoList,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubject(@PathVariable UUID id){
        String msg = subjectService.DeleteSubject(id);
        return  new ResponseEntity<>(msg,HttpStatus.OK);
    }
}
