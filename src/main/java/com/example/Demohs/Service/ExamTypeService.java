package com.example.Demohs.Service;

import com.example.Demohs.Dto.ExamTypeDto;
import com.example.Demohs.Entity.ExamType;

public interface ExamTypeService {
  ExamType findByExamName(String examType);

  ExamTypeDto createExamType(String examType);
}
