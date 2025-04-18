package com.example.Demohs.Repository;

import com.example.Demohs.Entity.ClassFeeStructureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassFeeStructureRepository extends JpaRepository<ClassFeeStructureEntity,Long> {

    Optional<ClassFeeStructureEntity> findByClassName(String className);
}
