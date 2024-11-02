package com.example.Demohs.Service;

import com.example.Demohs.Dto.SubjectDto;
import com.example.Demohs.Entity.Subject;

public interface SubjectService {
    Subject findBySubjectName(String subjectName);

    SubjectDto createSubject(String subjectName);
}
