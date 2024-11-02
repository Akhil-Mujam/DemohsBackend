package com.example.Demohs.Service;

import com.example.Demohs.Dto.ExamResultDto;
import com.example.Demohs.Entity.ExamResult;
import com.example.Demohs.Entity.ExamType;
import com.example.Demohs.Entity.StudentMaster;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ExamResultService {

    ExamResultDto findExamResult(String studentId);

    List<ExamResultDto> findByStudentMaster(String regNo);

    ExamResult findByStudentMasterAndExamType(StudentMaster studentMaster, ExamType  examTypeEntity);

    List<ExamResultDto> save(List<ExamResult> examResult);

    List<ExamResultDto> findByExamType(String examType);
}
