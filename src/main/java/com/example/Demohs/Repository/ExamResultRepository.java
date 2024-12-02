package com.example.Demohs.Repository;

import com.example.Demohs.Dto.ExamResultDto;
import com.example.Demohs.Entity.ExamResult;
import com.example.Demohs.Entity.ExamType;
import com.example.Demohs.Entity.StudentMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, UUID> {
    List<ExamResult> findByStudentMaster(StudentMaster studentMaster);

    ExamResult findByStudentMasterAndExamType(StudentMaster studentMaster, ExamType examTypeEntity);

    List<ExamResult> findByExamType(ExamType examTypeEntity);
}
