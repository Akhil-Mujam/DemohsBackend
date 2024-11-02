package com.example.Demohs.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Component
public class AttendanceDto {

    private String attendanceDate;  // The date of the attendance
    private List<StudentAttendance> studentAttendances;  // List of student attendances

    // Nested class for individual student attendance
    public static class StudentAttendance {
        private String regNo;  // Registration number of the student
        private boolean status;  // true for present, false for absent
    }

}
