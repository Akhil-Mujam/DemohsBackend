package com.example.Demohs.Repository;

import com.example.Demohs.Entity.ExamResult;
import com.example.Demohs.Entity.Subject;
import com.example.Demohs.Entity.SubjectMarks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubjectMarkRepository extends JpaRepository<SubjectMarks, UUID> { 
    Optional<SubjectMarks> findByExamResult(ExamResult examResult);

    Optional<SubjectMarks> findByExamResultAndSubject(ExamResult examResult, Subject subject);
}
