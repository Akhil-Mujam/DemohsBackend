package com.example.Demohs.Repository;

import com.example.Demohs.Entity.AllTeachers;
import com.example.Demohs.Entity.TeacherClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ClassTeacherRepository extends JpaRepository<TeacherClass, UUID> {

    boolean existsByTeacher_RegNo(String regNo);

    Optional<TeacherClass> findByClassEntity(String ClassName);


    TeacherClass findByTeacher(AllTeachers allTeachers);

    @Query("SELECT tc FROM TeacherClass tc WHERE tc.teacher.regNo = :regNo")
    Optional<TeacherClass> findTeacherClassByRegNo(@Param("regNo") String regNo);
}
