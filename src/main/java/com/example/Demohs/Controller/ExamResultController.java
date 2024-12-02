package com.example.Demohs.Controller;


import com.example.Demohs.Dto.ExamResultDto;
import com.example.Demohs.Entity.ExamResult;
import com.example.Demohs.Service.ExamResultService;
import com.example.Demohs.util.SheetToExamResultConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/examresult")
public class ExamResultController {

    @Autowired
    ExamResultService examResultService;
    @Autowired
    SheetToExamResultConvertor sheetToExamResultConvertor;
    @PostMapping("/{examType}/{teacherRegNo}")
    public ResponseEntity<String> createExamResult(@PathVariable String examType, @RequestParam MultipartFile file,@PathVariable String teacherRegNo) throws IOException {
      sheetToExamResultConvertor.saveExamResultsFromExcel(file,examType,teacherRegNo);
       return new ResponseEntity<>("Created SuccessFully",HttpStatus.CREATED);
    }

    @GetMapping("/stundentscore/{studentRegNo}")

    public ResponseEntity<List<ExamResultDto>> getResults(@PathVariable String studentRegNo)
    {
        System.out.println("getting request from frontend");
        List<ExamResultDto> examResultDtos=examResultService.findByStudentMaster(studentRegNo);
        return new ResponseEntity<>(examResultDtos,HttpStatus.OK);
    }
    @PutMapping("/{examType}/update/{teacherRegNo}")
    public ResponseEntity<String> updateMarks(@PathVariable String examType,@RequestParam MultipartFile file,@PathVariable String teacherRegNo)
    {
        sheetToExamResultConvertor.updateExamResultsFromExcel(file, examType,teacherRegNo);
        return new ResponseEntity<>("SuccessFully Updated",HttpStatus.OK);
    }
    @GetMapping("/rank/{examType}")
    public ResponseEntity<List<ExamResultDto>> getByRank(@PathVariable String examType)
    {
        List<ExamResultDto> examResultDtos=examResultService.findByExamType(examType);
        return new ResponseEntity<>(examResultDtos,HttpStatus.FOUND);
    }
}
