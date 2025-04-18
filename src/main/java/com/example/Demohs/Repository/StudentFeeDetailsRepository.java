package com.example.Demohs.Repository;

import com.example.Demohs.Entity.StudentFeeDetails;
import com.example.Demohs.Entity.StudentMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentFeeDetailsRepository extends JpaRepository<StudentFeeDetails, Long> {

    List<StudentFeeDetails> findByStudentMaster(StudentMaster studentMaster);
    

    Optional<StudentFeeDetails> findByStudentMasterAndTermName(StudentMaster studentMaster, String termName);
}
