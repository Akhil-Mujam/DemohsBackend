package com.example.Demohs.Repository;

import com.example.Demohs.Dto.AbsentStudentResponse;
import com.example.Demohs.Entity.Attendance;
import com.example.Demohs.Entity.StudentMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    Optional<Attendance> findByStudentMasterAndAttendanceDate(StudentMaster studentMaster, LocalDate date);

    boolean existsByAttendanceDateAndClassNameAndClassSection(LocalDate date, String classId, String classSection);



    //Get Absent List of a specific date to all students in the school
    @Query("SELECT new com.example.Demohs.Dto.AbsentStudentResponse(" +
            "a.studentMaster.regNo, " +
            "a.studentMaster.firstName, " +
            "a.studentMaster.lastName, " +
            "a.studentMaster.classesEntity, " +
            "a.studentMaster.classSection, " +
            "a.studentMaster.phno, " +
            "false) " +  // Hardcoded boolean value for the status field
            "FROM Attendance a " +
            "WHERE a.attendanceDate = :date AND a.status = false")
    List<AbsentStudentResponse> findAbsentStudentsByDate(@Param("date") LocalDate date);



    @Query("SELECT a FROM Attendance a WHERE a.studentMaster.classesEntity = :className")
    List<Attendance> findAttendanceByClassName(@Param("className") String className);

    // Get attendance by class name and specific date
//    @Query("SELECT a FROM Attendance a WHERE a.studentMaster.classesEntity = :className AND a.attendanceDate = :date")
//    List<Attendance> findAttendanceByClassNameAndDate(@Param("className") String className, @Param("date") LocalDate date);
    @Query("SELECT a FROM Attendance a WHERE a.className = :className AND a.classSection = :classSection AND a.attendanceDate = :date")
    List<Attendance> findAttendanceByClassNameAndSectionAndDate(@Param("className") String className,
                                                                @Param("classSection") String classSection,
                                                                @Param("date") LocalDate date);

    // Get attendance by student's registration number and specific date
    @Query("SELECT a FROM Attendance a WHERE a.studentMaster.regNo = :regNo AND a.attendanceDate = :date")
    List<Attendance> findAttendanceByRegNoAndDate(@Param("regNo") String regNo, @Param("date") LocalDate date);

    // Get attendance by student's registration number within a date range
    @Query("SELECT a FROM Attendance a WHERE a.studentMaster.regNo = :regNo AND a.attendanceDate BETWEEN :startDate AND :endDate")
    List<Attendance> findAttendanceByRegNoAndDateRange(@Param("regNo") String regNo, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
