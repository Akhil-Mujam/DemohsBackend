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
@RestController
@RequestMapping("/examresult")
public class ExamResultController {

    @Autowired
    ExamResultService examResultService;

    @Autowired
    SheetToExamResultConvertor sheetToExamResultConvertor;
    @PostMapping("/{examType}")
    public ResponseEntity<List<ExamResultDto>> createExamResult(@PathVariable String examType, @RequestParam MultipartFile file) throws IOException {
       List<ExamResult> examResultList=sheetToExamResultConvertor.saveExamResultsFromExcel(file,examType);
       List<ExamResultDto> examResultDtos=examResultService.save(examResultList);
       return new ResponseEntity<>(examResultDtos,HttpStatus.CREATED);
    }
    @GetMapping("/stundentscore/{studentRegNo}")
    public ResponseEntity<List<ExamResultDto>> getResults(@PathVariable String studentRegNo)
    {
        List<ExamResultDto> examResultDtos=examResultService.findByStudentMaster(studentRegNo);
        return new ResponseEntity<>(examResultDtos,HttpStatus.FOUND);
    }

    @PutMapping("/{examType}")
    public ResponseEntity<String> updateMarks(@PathVariable String examType,@RequestParam MultipartFile file)
    {
        sheetToExamResultConvertor.updateExamResultsFromExcel(file, examType);
        return new ResponseEntity<>("SuccessFully Updated",HttpStatus.OK);
    }
    @GetMapping("/rank/{examType}")
    public ResponseEntity<List<ExamResultDto>> getByRank(@PathVariable String examType)
    {
        List<ExamResultDto> examResultDtos=examResultService.findByExamType(examType);
        return new ResponseEntity<>(examResultDtos,HttpStatus.FOUND);
    }

}
