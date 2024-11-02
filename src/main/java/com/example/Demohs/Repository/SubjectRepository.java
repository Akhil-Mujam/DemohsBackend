package com.example.Demohs.Repository;

import com.example.Demohs.Entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject,Long> {
    Subject findBySubjectName(String subjectName);
}
