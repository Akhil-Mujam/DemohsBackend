package com.example.Demohs.Controller;

import com.example.Demohs.Dto.AbsentStudentResponse;
import com.example.Demohs.Dto.BatchAttendanceDTO;
import com.example.Demohs.Dto.StudentAttendanceResponse;
import com.example.Demohs.Entity.Attendance;
import com.example.Demohs.Service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/student/attendance")

public class AttendanceController {

    @Autowired
    public AttendanceService attendanceService;

//      add attendance
//         {
//        "attendanceDate":"2024-10-26",
//            "className":"III",
//            "classSection":"A",
//            "studentAttendances":
//            [
//                  {
//                    "regNo":"IIIA02",
//                    "status":"false"
//                   }
//             ]
//          }
    @PostMapping("/markBatch")
    public ResponseEntity<String> markBatchAttendance(@RequestBody BatchAttendanceDTO batchAttendanceDTO) {
        // Process the batch attendance here

       String s = attendanceService.markBatchAttendance(batchAttendanceDTO);

        // Example: Log the data
        System.out.println("Attendance Date: " + batchAttendanceDTO.getAttendanceDate());
        batchAttendanceDTO.getStudentAttendances().forEach(attendance -> {
            System.out.println("RegNo: " + attendance.getRegNo() + " | Status: " + attendance.isStatus());
        });

        // Return response (replace with actual logic)
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

    @GetMapping("/checkToday/{classId}/{classSection}")
    public ResponseEntity<Boolean> CheckTodayAttendance(@PathVariable String classId,@PathVariable String classSection){

            return new ResponseEntity<>(attendanceService.CheckTodayAttendance( classId,classSection),HttpStatus.OK);
    }


    @PutMapping("/updateBatch")
    public ResponseEntity<String> updateBatchAttendance(@RequestBody BatchAttendanceDTO batchUpdateDTO) {
       String s = attendanceService.updateBatchAttendance(batchUpdateDTO);
        return new ResponseEntity<>(s,HttpStatus.OK);
    }

    // Endpoint to get attendance by class name and date
    @GetMapping("/byClass/{className}/{classSection}/date/{date}")
    public ResponseEntity<List<Attendance>> getAttendanceByClassNameAndDate(
            @PathVariable String className,@PathVariable String classSection, @PathVariable String date) {
        LocalDate attendanceDate = LocalDate.parse(date);
        List<Attendance> attendanceRecords = attendanceService.getAttendanceByClassNameAndDate(className, classSection,attendanceDate);
        return ResponseEntity.ok(attendanceRecords);
    }

    // Endpoint to get attendance by student regNo and date
   
    @GetMapping("/byStudent/{regNo}/date/{date}")
    public ResponseEntity<List<Attendance>> getAttendanceByRegNoAndDate(
            @PathVariable String regNo, @PathVariable String date) {
        LocalDate attendanceDate = LocalDate.parse(date);
        List<Attendance> attendanceRecords = attendanceService.getAttendanceByRegNoAndDate(regNo, attendanceDate);
        return ResponseEntity.ok(attendanceRecords);
    }

    // Endpoint to get attendance by student regNo within a date range
    @GetMapping("/byStudent/{regNo}/from/{fromDate}/to/{toDate}")
    public ResponseEntity<StudentAttendanceResponse> getAttendanceByStudent(
            @PathVariable String regNo,
            @PathVariable String fromDate,
            @PathVariable String toDate) {

        LocalDate start = LocalDate.parse(fromDate);
        LocalDate end = LocalDate.parse(toDate);

        StudentAttendanceResponse response = attendanceService.getAttendanceByStudent(regNo, start , end);

        return ResponseEntity.ok(Objects.requireNonNullElseGet(response, () -> new StudentAttendanceResponse("No Data", "", "", List.of())));

    }

    //List of absentees
    @GetMapping("/absentStudents")
    public List<AbsentStudentResponse> getAbsentStudents(@RequestParam String date) {
        return attendanceService.getAbsentStudentsByDate(LocalDate.parse(date));
    }

}
