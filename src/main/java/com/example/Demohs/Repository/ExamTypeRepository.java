package com.example.Demohs.Repository;

import com.example.Demohs.Entity.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExamTypeRepository extends JpaRepository<ExamType, UUID> {
    ExamType findByExamName(String examName);
}
