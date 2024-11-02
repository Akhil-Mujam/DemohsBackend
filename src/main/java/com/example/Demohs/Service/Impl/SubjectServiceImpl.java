package com.example.Demohs.Service.Impl;

import com.example.Demohs.Dto.SubjectDto;
import com.example.Demohs.Entity.Subject;
import com.example.Demohs.Repository.SubjectRepository;
import com.example.Demohs.Service.SubjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public Subject findBySubjectName(String subjectName) {
        return  subjectRepository.findBySubjectName(subjectName);
    }

    @Override
    public SubjectDto createSubject(String subjectName) {
        Subject subject=new Subject();
        subject.setSubjectName(subjectName);
        return modelMapper.map(subjectRepository.save(subject),SubjectDto.class);
    }
}
