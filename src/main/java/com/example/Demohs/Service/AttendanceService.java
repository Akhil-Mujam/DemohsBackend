package com.example.Demohs.Service;

import com.example.Demohs.Dto.AbsentStudentResponse;
import com.example.Demohs.Dto.BatchAttendanceDTO;
import com.example.Demohs.Dto.StudentAttendanceResponse;
import com.example.Demohs.Entity.Attendance;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {

    public String markBatchAttendance(BatchAttendanceDTO batchAttendanceDTO);

    public String updateBatchAttendance(BatchAttendanceDTO batchUpdateDTO);


    public StudentAttendanceResponse getAttendanceByStudent(String regNo, LocalDate fromDate, LocalDate toDate);

    public List<Attendance> getAttendanceByRegNoAndDate(String regNo, LocalDate date);

    public List<Attendance> getAttendanceByClassNameAndDate(String className, LocalDate date);

    public List<AbsentStudentResponse> getAbsentStudentsByDate(LocalDate date);
}
