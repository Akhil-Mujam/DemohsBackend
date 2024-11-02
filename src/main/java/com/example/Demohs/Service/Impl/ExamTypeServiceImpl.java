package com.example.Demohs.Service.Impl;

import com.example.Demohs.Dto.ExamTypeDto;
import com.example.Demohs.Entity.ExamType;
import com.example.Demohs.Repository.ExamTypeRepository;
import com.example.Demohs.Service.ExamTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamTypeServiceImpl implements ExamTypeService {
    @Autowired
    ExamTypeRepository examTypeRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public ExamType findByExamName(String examName) {
        return examTypeRepository.findByExamName(examName);
    }

    @Override
    public ExamTypeDto createExamType(String examName) {
        ExamType examType=new ExamType();
        examType.setExamName(examName);
        return modelMapper.map(examTypeRepository.save(examType),ExamTypeDto.class);
    }
}
