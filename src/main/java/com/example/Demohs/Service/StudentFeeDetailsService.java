package com.example.Demohs.Service;

import com.example.Demohs.Dto.UpdateFeeDetailRequest;
import com.example.Demohs.Entity.ClassFeeStructureEntity;
import com.example.Demohs.Entity.StudentMaster;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface StudentFeeDetailsService {

    public void initializeFeeDetailsForStudent(StudentMaster student, List<ClassFeeStructureEntity> classFeeStructures);

    public List<Map<String, Object>> getStudentsWithFeeDetailsByClassAndSection(String className, String classSection);

    public void updateFeePaymentStatus(Long feeDetailId, Boolean isPaid, LocalDate paidDate);

    public List<Map<String, Object>> getStudentFeeDetails(UUID studentId);

    public void updateStudentFeeDetails(UUID studentId, List<UpdateFeeDetailRequest> feeDetails);


}
