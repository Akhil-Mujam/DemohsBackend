package com.example.Demohs.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// DTO for batch attendance submission
@Data  // Lombok annotation for getters, setters, toString, equals, and hashCode
@NoArgsConstructor  // Lombok annotation for no-args constructor
@AllArgsConstructor  // Lombok annotation for all-args constructor
@Builder
public class BatchAttendanceDTO {

    private String attendanceDate;  // The date of the attendance

    private String className;

    private String classSection;

    private List<StudentAttendance> studentAttendances;  // List of student attendance

    // Inner class to represent individual student attendance data
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentAttendance {
        private String regNo;  // Registration number of the student
        private boolean status;  // true for present, false for absent
    }
}
