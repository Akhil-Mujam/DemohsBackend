package com.example.Demohs.Service.Impl;

import com.example.Demohs.Dto.AbsentStudentResponse;
import com.example.Demohs.Dto.BatchAttendanceDTO;
import com.example.Demohs.Dto.StudentAttendanceResponse;
import com.example.Demohs.Entity.Attendance;
import com.example.Demohs.Entity.StudentMaster;
import com.example.Demohs.Exception.AttendanceRecordNotFoundException;
import com.example.Demohs.Exception.InvalidAttendanceUpdateException;
import com.example.Demohs.Exception.ResourceNotFoundException;
import com.example.Demohs.Repository.AttendanceRepository;
import com.example.Demohs.Repository.StudentMasterRepository;
import com.example.Demohs.Service.AttendanceService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentMasterRepository studentMasterRepository;

    public String markBatchAttendance(BatchAttendanceDTO batchAttendanceDTO) {
        LocalDate date = LocalDate.parse(batchAttendanceDTO.getAttendanceDate());

        String className = batchAttendanceDTO.getClassName();
        String classSection = batchAttendanceDTO.getClassSection();

        for (BatchAttendanceDTO.StudentAttendance studentAttendance : batchAttendanceDTO.getStudentAttendances()) {
            // Fetch the student from the database
            StudentMaster studentMaster = studentMasterRepository.findByRegNo(studentAttendance.getRegNo())
                    .orElseThrow(() ->   new ResourceNotFoundException("Student not found"));

            // Create a new attendance record
            Attendance attendance = new Attendance();
            attendance.setStudentMaster(studentMaster);
            attendance.setAttendanceDate(date);
            attendance.setClassName(className);
            attendance.setClassSection(classSection);
            attendance.setStatus(studentAttendance.isStatus());

            // Save the attendance record to the database
            attendanceRepository.save(attendance);
        }

        return "attendance is saved successfully";

    }

    @Transactional
    public String updateBatchAttendance(BatchAttendanceDTO batchUpdateDTO) {

        LocalDate today = LocalDate.now();
        LocalDate date = LocalDate.parse(batchUpdateDTO.getAttendanceDate());

        System.out.println(date + " today = "+today);

        if (!today.equals(date)) {
            throw new InvalidAttendanceUpdateException("Cannot update attendance for a date other than today.");
        }

        List<BatchAttendanceDTO.StudentAttendance> studentUpdates = batchUpdateDTO.getStudentAttendances();

        // check whether the class and section is correct or not

        for (BatchAttendanceDTO.StudentAttendance studentUpdate : studentUpdates) {

            // Retrieve student by regNo
            StudentMaster studentMaster = studentMasterRepository.findByRegNo(studentUpdate.getRegNo())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentUpdate.getRegNo()));

            // Retrieve attendance record for the specified date
            Attendance attendance = attendanceRepository.findByStudentMasterAndAttendanceDate(studentMaster, date)
                    .orElseThrow(() -> new AttendanceRecordNotFoundException("Attendance record not found for student: " + studentUpdate.getRegNo()));

            // Update the status
            attendance.setStatus(studentUpdate.isStatus());

            // Save the updated attendance record
            attendanceRepository.save(attendance);
        }

        return "Batch attendance updated successfully";
    }




    // Get attendance by class name and date
    public List<Attendance> getAttendanceByClassNameAndDate(String className, LocalDate date) {
        return attendanceRepository.findAttendanceByClassNameAndDate(className, date);
    }

    @Override
    public List<AbsentStudentResponse> getAbsentStudentsByDate(LocalDate date) {

            return attendanceRepository.findAbsentStudentsByDate(date);

    }

    // Get attendance by student registration number and date
    public List<Attendance> getAttendanceByRegNoAndDate(String regNo, LocalDate date) {
        return attendanceRepository.findAttendanceByRegNoAndDate(regNo, date);
    }

    // Get attendance by student registration number within a date range
    public StudentAttendanceResponse getAttendanceByStudent(String regNo, LocalDate fromDate, LocalDate toDate) {
        List<Attendance> attendanceList = attendanceRepository.findAttendanceByRegNoAndDateRange(regNo,fromDate,toDate);

        if (attendanceList.isEmpty()) {
            return null; // Or handle as appropriate
        }

        String studentName = attendanceList.get(0).getStudentMaster().getFirstName() + " " + attendanceList.get(0).getStudentMaster().getLastName();
        String className = attendanceList.get(0).getClassName();
       String classSection = attendanceList.get(0).getClassSection();

        List<StudentAttendanceResponse.AttendanceInfo> attendanceDetails = attendanceList.stream()
                .map(att -> new StudentAttendanceResponse.AttendanceInfo(att.getAttendanceDate().toString(),att.isStatus()))
                .collect(Collectors.toList());

        return new StudentAttendanceResponse(studentName, className, classSection,attendanceDetails);
    }
}