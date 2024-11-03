package com.example.Demohs.Repository;

import com.example.Demohs.Entity.AllTeachers;
import com.example.Demohs.Entity.TeacherClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassTeacherRepository extends JpaRepository<TeacherClass,Long> {

    boolean existsByTeacher_RegNo(String regNo);

    Optional<TeacherClass> findByClassEntity(String ClassName);


    TeacherClass findByTeacher(AllTeachers allTeachers);
}
