package com.example.Demohs.Service.Impl;

import com.example.Demohs.Dto.SubjectDto;
import com.example.Demohs.Entity.Subject;
import com.example.Demohs.Repository.SubjectRepository;
import com.example.Demohs.Service.SubjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public Subject findBySubjectName(String subjectName) {
        subjectName=subjectName.toUpperCase();
        return subjectRepository.findBySubjectName(subjectName);
    }

    @Override
    public SubjectDto createSubject(String subjectName) {
        subjectName=subjectName.toUpperCase();
        Subject subject=new Subject();
        subject.setSubjectId(UUID.randomUUID());
        subject.setSubjectName(subjectName);
        SubjectDto subjectDto = modelMapper.map(subjectRepository.save(subject),SubjectDto.class);
        return subjectDto;
    }
}
