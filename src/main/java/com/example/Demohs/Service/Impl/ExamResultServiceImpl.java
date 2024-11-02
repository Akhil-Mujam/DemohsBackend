package com.example.Demohs.Service.Impl;

import com.example.Demohs.Dto.ExamResultDto;
import com.example.Demohs.Dto.ExamTypeDto;
import com.example.Demohs.Entity.ExamResult;
import com.example.Demohs.Entity.ExamType;
import com.example.Demohs.Entity.StudentMaster;
import com.example.Demohs.Exception.ResourceNotFoundException;
import com.example.Demohs.Repository.ExamResultRepository;
import com.example.Demohs.Repository.StudentMasterRepository;
import com.example.Demohs.Service.ExamResultService;
import com.example.Demohs.Service.ExamTypeService;
import com.example.Demohs.util.SheetToExamResultConvertor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ExamResultServiceImpl implements ExamResultService {
    @Autowired
    ExamResultRepository examResultRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    StudentMasterRepository studentMasterRepository;

    @Autowired
    ExamTypeService examTypeService;
    @Override
    public ExamResultDto findExamResult(String studentId) {
        return null;
    }

    @Override
    public List<ExamResultDto> findByStudentMaster(String regNo) {
        Optional<StudentMaster> studentMaster=studentMasterRepository.findByRegNo(regNo);
        List<ExamResultDto> examResultDtos=new ArrayList<>();
        if(!studentMaster.isPresent())throw new ResourceNotFoundException("Student Not found ");
        List<ExamResult> examResultList=examResultRepository.findByStudentMaster(studentMaster.get());
        examResultList.forEach(examResult -> {
           ExamResultDto examResultDto= modelMapper.map(examResult,ExamResultDto.class);
           examResultDto.setRegNo(examResult.getStudentMaster().getRegNo());
            examResultDtos.add(examResultDto);
        });
        return examResultDtos;
    }

    @Override
    public ExamResult findByStudentMasterAndExamType(StudentMaster student, ExamType examTypeEntity) {
        return examResultRepository.findByStudentMasterAndExamType(student,examTypeEntity);
    }

    @Override
    public List<ExamResultDto> save(List<ExamResult> examResultList) {
        List<ExamResult> examResultList1=examResultRepository.saveAll(examResultList);
        List<ExamResultDto> examResultDtos=new ArrayList<>();
        examResultList1.forEach(examResult -> {
            examResultDtos.add(modelMapper.map(examResult,ExamResultDto.class));
        });
        return examResultDtos;
    }

    @Override
    public List<ExamResultDto> findByExamType(String examType) {
         ExamType examTypeEntity=examTypeService.findByExamName(examType);
         List<ExamResult> examResultList=  examResultRepository.findByExamType(examTypeEntity);
         List<ExamResultDto> examResultDtos=new ArrayList<>();
         examResultList.sort(Comparator.comparing(ExamResult::getTotal).reversed());
         examResultList.forEach(examResult -> {
               ExamResultDto examResultDto= modelMapper.map(examResult,ExamResultDto.class);
               examResultDto.setRegNo(examResult.getStudentMaster().getRegNo());
               examResultDtos.add(examResultDto);
        });
          return examResultDtos;
    }

}
