package com.example.Demohs.Repository;

import com.example.Demohs.Entity.StudentMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentMasterRepository extends JpaRepository<StudentMaster,String> {

       Optional<StudentMaster> findByRegNo(String regNo);

//       List<StudentMaster> findByClassesEntity(String ClassId);

       Page<StudentMaster> findByClassesEntityAndClassSection(String classId, String classSection, Pageable pageable);
}
