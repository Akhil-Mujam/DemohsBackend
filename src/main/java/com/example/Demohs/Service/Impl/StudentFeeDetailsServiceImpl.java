package com.example.Demohs.Service.Impl;

import com.example.Demohs.Dto.UpdateFeeDetailRequest;
import com.example.Demohs.Entity.ClassFeeStructureEntity;
import com.example.Demohs.Entity.StudentFeeDetails;
import com.example.Demohs.Entity.StudentMaster;
import com.example.Demohs.Repository.StudentFeeDetailsRepository;
import com.example.Demohs.Repository.StudentMasterRepository;
import com.example.Demohs.Service.StudentFeeDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentFeeDetailsServiceImpl implements StudentFeeDetailsService {

    private final StudentMasterRepository studentMasterRepository;
    private final StudentFeeDetailsRepository studentFeeDetailsRepository;

    // Create fee details for a student during creation
    public void initializeFeeDetailsForStudent(StudentMaster student, List<ClassFeeStructureEntity> classFeeStructures) {

        for (ClassFeeStructureEntity feeStructure : classFeeStructures) {
            StudentFeeDetails term1 = StudentFeeDetails.builder()
                    .studentMaster(student)
                    .termName("1st Term")
                    .amount(feeStructure.getTerm1Fee())

                    .isPaid(false)
                    .build();

            StudentFeeDetails term2 = StudentFeeDetails.builder()
                    .studentMaster(student)
                    .termName("2nd Term")
                    .amount(feeStructure.getTerm2Fee())

                    .isPaid(false)
                    .build();

            StudentFeeDetails term3 = StudentFeeDetails.builder()
                    .studentMaster(student)
                    .termName("3rd Term")
                    .amount(feeStructure.getTerm3Fee())

                    .isPaid(false)
                    .build();

            studentFeeDetailsRepository.saveAll(List.of(term1, term2, term3));
        }
    }

    // Get all students with fee details by class and section
//    public List<Map<String, Object>> getStudentsWithFeeDetailsByClassAndSection(String className, String classSection) {
//        // Fetch all students for the given class and section
//        List<StudentMaster> students = studentMasterRepository.findByClassesEntityAndClassSection(className, classSection);
//
//        // Prepare the result list
//        List<Map<String, Object>> result = new ArrayList<>();
//
//        // Process each student
//        for (StudentMaster student : students) {
//            // Fetch fee details for the student
//            List<StudentFeeDetails> feeDetails = studentFeeDetailsRepository.findByStudentMaster(student);
//
//            // Create a result map for the student
//            Map<String, Object> studentData = new LinkedHashMap<>();
//            studentData.put("Name", student.getFirstName() + " " + student.getLastName());
//
//            // Add term fee details
//            for (StudentFeeDetails feeDetail : feeDetails) {
//                switch (feeDetail.getTermName()) {
//                    case "1st Term":
//                        studentData.put("1st Term", feeDetail.getAmount());
//                        studentData.put("Paid Date (1st Term)", feeDetail.getPaidDate());
//                        break;
//                    case "2nd Term":
//                        studentData.put("2nd Term", feeDetail.getAmount());
//                        studentData.put("Paid Date (2nd Term)", feeDetail.getPaidDate());
//                        break;
//                    case "3rd Term":
//                        studentData.put("3rd Term", feeDetail.getAmount());
//                        studentData.put("Paid Date (3rd Term)", feeDetail.getPaidDate());
//                        break;
//                }
//            }
//
//            // Calculate total fee and discount
//            int totalFee = feeDetails.stream().mapToInt(StudentFeeDetails::getAmount).sum();
//            int totalDiscount = feeDetails.stream()
//                    .mapToInt(fee -> fee.getDiscount() != null ? fee.getDiscount() : 0)
//                    .sum();
//
//            studentData.put("Total Fee", totalFee);
//            studentData.put("Discount", totalDiscount);
//            studentData.put("Fee After Discount", totalFee - totalDiscount);
//
//            // Add to result list
//            result.add(studentData);
//        }
//
//        return result;
//    }


    // Get all students with fee details by class and section
    public List<Map<String, Object>> getStudentsWithFeeDetailsByClassAndSection(String className, String classSection) {
        // Fetch all students for the given class and section
        List<StudentMaster> students = studentMasterRepository.findByClassesEntityAndClassSection(className, classSection);

        // Prepare the result list
        List<Map<String, Object>> result = new ArrayList<>();

        // Process each student
        for (StudentMaster student : students) {
            // Fetch fee details for the student
            List<StudentFeeDetails> feeDetails = studentFeeDetailsRepository.findByStudentMaster(student);

            // Create a result map for the student
            Map<String, Object> studentData = new LinkedHashMap<>();
            studentData.put("Name", (student.getFirstName() != null ? student.getFirstName() : "") +
                    " " +
                    (student.getLastName() != null ? student.getLastName() : ""));
            studentData.put("Student Id",student.getStudentId());

            // Add term fee details as nested objects
            for (StudentFeeDetails feeDetail : feeDetails) {
                Map<String, Object> termDetails = new LinkedHashMap<>();
                termDetails.put("amount", feeDetail.getAmount() != null ? feeDetail.getAmount() : 0);
                termDetails.put("paidDate", feeDetail.getPaidDate() != null ? feeDetail.getPaidDate() : "N/A");
                termDetails.put("feeDetailId", feeDetail.getId());
                studentData.put(feeDetail.getTermName(), termDetails);
            }

            // Calculate total fee and discount
            int totalFee = feeDetails.stream()
                    .mapToInt(fee -> fee.getAmount() != null ? fee.getAmount() : 0)
                    .sum();

            int studentDiscount = student.getDiscount() != null ? student.getDiscount() : 0;
            studentData.put("Total Fee", totalFee);
            studentData.put("Discount", studentDiscount);
            studentData.put("Fee After Discount", totalFee - studentDiscount);

            // Add to result list
            result.add(studentData);
        }

        return result;
    }




    // Update fee payment status for a term
    public void updateFeePaymentStatus(Long feeDetailId, Boolean isPaid, LocalDate paidDate) {
        StudentFeeDetails feeDetail = studentFeeDetailsRepository.findById(feeDetailId)
                .orElseThrow(() -> new IllegalArgumentException("Fee detail not found"));

        feeDetail.setIsPaid(isPaid);
        feeDetail.setPaidDate(paidDate);
       // feeDetail.setDiscount(discount==null?0:discount);
        studentFeeDetailsRepository.save(feeDetail);
    }

    @Override
    public List<Map<String, Object>> getStudentFeeDetails(UUID studentId) {
        StudentMaster student = studentMasterRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        List<StudentFeeDetails> feeDetails = studentFeeDetailsRepository.findByStudentMaster(student);

        int studentDiscount = student.getDiscount() != null ? student.getDiscount() : 0;

        return feeDetails.stream().map(fee -> {
            Map<String, Object> detail = new LinkedHashMap<>();
            detail.put("Term", fee.getTermName());
            detail.put("Paid Date", fee.getPaidDate());
            detail.put("Amount", fee.getAmount());
            detail.put("Is Paid", fee.getIsPaid());
            detail.put("Discount", studentDiscount);
            return detail;
        }).collect(Collectors.toList());
    }

    public void updateStudentFeeDetails(UUID studentId, List<UpdateFeeDetailRequest> feeDetails) {
        StudentMaster student = studentMasterRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        for (UpdateFeeDetailRequest detailRequest : feeDetails) {
            StudentFeeDetails feeDetail = studentFeeDetailsRepository.findByStudentMasterAndTermName(student, detailRequest.getTermName())
                    .orElseThrow(() -> new IllegalArgumentException("Fee detail not found for term: " + detailRequest.getTermName()));

            feeDetail.setPaidDate(detailRequest.getPaidDate());
         //   feeDetail.setDiscount(detailRequest.getDiscount());
            feeDetail.setIsPaid(detailRequest.getIsPaid());

            studentFeeDetailsRepository.save(feeDetail);
        }
    }




}
