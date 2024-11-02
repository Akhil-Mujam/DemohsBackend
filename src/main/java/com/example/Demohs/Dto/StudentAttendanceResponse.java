package com.example.Demohs.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentAttendanceResponse {
    private String student;
    private String className;
    private String classSection;
    private List<AttendanceInfo> attendanceDetails;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
   public static class AttendanceInfo {
        private String attendanceDate;
        private boolean status;
    }
}





