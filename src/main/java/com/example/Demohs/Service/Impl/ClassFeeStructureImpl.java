package com.example.Demohs.Service.Impl;

import com.example.Demohs.Entity.ClassFeeStructureEntity;
import com.example.Demohs.Entity.StudentFeeDetails;
import com.example.Demohs.Entity.StudentMaster;
import com.example.Demohs.Exception.ResourceNotFoundException;
import com.example.Demohs.Repository.ClassFeeStructureRepository;
import com.example.Demohs.Repository.StudentFeeDetailsRepository;
import com.example.Demohs.Repository.StudentMasterRepository;
import com.example.Demohs.Service.ClassFeeStructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ClassFeeStructureImpl implements ClassFeeStructureService {

    @Autowired
    ClassFeeStructureRepository classFeeStructureRepository;

    @Autowired
    StudentMasterRepository studentMasterRepository;

    @Autowired
    StudentFeeDetailsRepository studentFeeDetailsRepository;


    public ClassFeeStructureEntity createOrUpdateFeeStructure(ClassFeeStructureEntity feeStructure) {
        String className = feeStructure.getClassName();

        // Allowed class names
        List<String> allowedClasses = Arrays.asList("Nursery", "LKG", "UKG",
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

        // Check if className is valid
        if (!allowedClasses.contains(className)) {
            throw new IllegalArgumentException("Invalid class name: " + className +
                    ". Class name should be between Nursery and 10.");
        }

        // Check if fee structure already exists
        Optional<ClassFeeStructureEntity> existingFeeStructure = classFeeStructureRepository.findByClassName(className);
        if (existingFeeStructure.isPresent()) {
            throw new IllegalArgumentException("Class Fee Structure already exists for: " + className);
        }

        return classFeeStructureRepository.save(feeStructure);
    }


    @Override
    public List<ClassFeeStructureEntity> getAllClassesFeeStrutures() {
        // Fetch all class fee structures from the repository
        List<ClassFeeStructureEntity> feeStructures = classFeeStructureRepository.findAll();

        // Check if the list is empty and handle the case
        if (feeStructures.isEmpty()) {
            throw new ResourceNotFoundException("No class fee structures found.");
        }

        return feeStructures;
    }

    // Retrieve a fee structure by class name
    public Optional<ClassFeeStructureEntity> getFeeStructureByClassName(String className) {
        return classFeeStructureRepository.findByClassName(className);
    }

    // Update term fees
    // Update term fees and propagate changes to StudentFeeDetails
    public ClassFeeStructureEntity updateTermFees(String className, Integer term1Fee, Integer term2Fee, Integer term3Fee) {
        // Fetch and update the fee structure
        ClassFeeStructureEntity feeStructure = classFeeStructureRepository.findByClassName(className)
                .orElseThrow(() -> new IllegalArgumentException("Class not found: " + className));

        feeStructure.setTerm1Fee(term1Fee);
        feeStructure.setTerm2Fee(term2Fee);
        feeStructure.setTerm3Fee(term3Fee);

        // Save updated fee structure
        ClassFeeStructureEntity updatedStructure = classFeeStructureRepository.save(feeStructure);

        // Update StudentFeeDetails for all students in this class
        List<StudentMaster> studentsInClass = studentMasterRepository.findByClassesEntity(className);

        for (StudentMaster student : studentsInClass) {
            List<StudentFeeDetails> studentFeeDetails = studentFeeDetailsRepository.findByStudentMaster(student);

            for (StudentFeeDetails feeDetail : studentFeeDetails) {
                if (feeDetail.getTermName().equalsIgnoreCase("1st Term")) {
                    feeDetail.setAmount(term1Fee);
                } else if (feeDetail.getTermName().equalsIgnoreCase("2nd Term")) {
                    feeDetail.setAmount(term2Fee);
                } else if (feeDetail.getTermName().equalsIgnoreCase("3rd Term")) {
                    feeDetail.setAmount(term3Fee);
                }
                studentFeeDetailsRepository.save(feeDetail); // Save updated fee details
            }
        }

        return updatedStructure;
    }


    @Override
    public void updateClassFeeStructure(String className, int term1Fee, int term2Fee, int term3Fee) {
        ClassFeeStructureEntity feeStructure = classFeeStructureRepository
                .findByClassName(className)
                .orElseThrow(() -> new ResourceNotFoundException("Fee structure not found for class " + className));

        feeStructure.setTerm1Fee(term1Fee);
        feeStructure.setTerm2Fee(term2Fee);
        feeStructure.setTerm3Fee(term3Fee);

        classFeeStructureRepository.save(feeStructure);
    }


}
