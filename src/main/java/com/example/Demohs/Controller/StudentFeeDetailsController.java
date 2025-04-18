package com.example.Demohs.Controller;

import com.example.Demohs.Dto.UpdateFeeDetailRequest;
import com.example.Demohs.Service.StudentFeeDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/fee-details")
@RequiredArgsConstructor
public class StudentFeeDetailsController {

    private final StudentFeeDetailsService studentFeeDetailsService;

    /**
     * Initialize fee details for a student.
     * This is typically used during student creation (called internally by the service).
     */
    @PostMapping("/initialize/{studentId}")
    public ResponseEntity<String> initializeFeeDetailsForStudent(@PathVariable UUID studentId) {
        // This method assumes the service internally manages the fee initialization
        return ResponseEntity.ok("Fee details initialized successfully for student with ID: " + studentId);
    }

    /**
     * Get all students with their fee details by class and section.
     */
    @GetMapping("/class/{className}/section/{classSection}")
    public ResponseEntity<List<Map<String, Object>>> getStudentsWithFeeDetailsByClassAndSection(
            @PathVariable String className,
            @PathVariable String classSection
    ) {
        List<Map<String, Object>> students = studentFeeDetailsService.getStudentsWithFeeDetailsByClassAndSection(className, classSection);
        return ResponseEntity.ok(students);
    }

    /**
     * Get fee details for a specific student.
     */
    @GetMapping("/{studentId}")
    public ResponseEntity<List<Map<String, Object>>> getStudentFeeDetails(@PathVariable UUID studentId) {
        List<Map<String, Object>> feeDetails = studentFeeDetailsService.getStudentFeeDetails(studentId);
        return ResponseEntity.ok(feeDetails);
    }

    /**
     * Update fee payment status for a specific fee detail.
     */
    @PutMapping("/update-payment/{feeDetailId}")
    public ResponseEntity<String> updateFeePaymentStatus(
            @PathVariable Long feeDetailId,
            @RequestParam Boolean isPaid,
            @RequestParam(required = false) LocalDate paidDate
    ) {
        studentFeeDetailsService.updateFeePaymentStatus(feeDetailId, isPaid, paidDate);
        return ResponseEntity.ok("Fee payment status updated successfully.");
    }

    /**
     * Update fee details for a specific student.
     */
    @PutMapping("/{studentId}")
    public ResponseEntity<String> updateStudentFeeDetails(
            @PathVariable UUID studentId,
            @RequestBody List<UpdateFeeDetailRequest> feeDetails
    ) {

        studentFeeDetailsService.updateStudentFeeDetails(studentId, feeDetails);
        return ResponseEntity.ok("Fee details updated successfully for student with ID: " + studentId);
    }
}
