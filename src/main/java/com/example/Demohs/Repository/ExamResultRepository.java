package com.example.Demohs.Repository;

import com.example.Demohs.Dto.ExamResultDto;
import com.example.Demohs.Entity.ExamResult;
import com.example.Demohs.Entity.ExamType;
import com.example.Demohs.Entity.StudentMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult,Long> {
    List<ExamResult> findByStudentMaster(StudentMaster studentMaster);

    ExamResult findByStudentMasterAndExamType(StudentMaster studentMaster, ExamType examTypeEntity);

    List<ExamResult> findByExamType(ExamType examTypeEntity);
}
