package com.example.Demohs.Service;

import com.example.Demohs.Dto.SubjectDto;
import com.example.Demohs.Entity.Subject;

import java.util.List;
import java.util.UUID;

public interface SubjectService {
    Subject findBySubjectName(String subjectName);

    SubjectDto createSubject(String subjectName);

    List<SubjectDto> getAllSubjects();

    String DeleteSubject(UUID id);
}
