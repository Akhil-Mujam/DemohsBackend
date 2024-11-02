package com.example.Demohs.Repository;

import com.example.Demohs.Entity.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamTypeRepository extends JpaRepository<ExamType,Long> {
    ExamType findByExamName(String examName);
}
