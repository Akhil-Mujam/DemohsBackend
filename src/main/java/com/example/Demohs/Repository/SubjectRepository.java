package com.example.Demohs.Repository;

import com.example.Demohs.Entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface SubjectRepository extends JpaRepository<Subject, UUID> {
    @Query("SELECT s FROM Subject s WHERE s.subjectName LIKE CONCAT('%', :subjectName, '%')")
    Subject findBySubjectName(@Param("subjectName") String subjectName);
}
