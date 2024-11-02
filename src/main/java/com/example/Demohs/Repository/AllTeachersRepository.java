package com.example.Demohs.Repository;

import com.example.Demohs.Entity.AllTeachers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AllTeachersRepository extends JpaRepository<AllTeachers,Long> {

    public Optional<AllTeachers> findByRegNo(String regNo);
}
