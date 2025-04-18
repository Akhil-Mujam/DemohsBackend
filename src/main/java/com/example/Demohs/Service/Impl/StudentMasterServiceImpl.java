package com.example.Demohs.Service.Impl;

import com.example.Demohs.Dto.StudentMasterDto;
import com.example.Demohs.Dto.UserAuthDataDto;
import com.example.Demohs.Entity.ClassFeeStructureEntity;
import com.example.Demohs.Entity.StudentMaster;
import com.example.Demohs.Entity.UserAuthData;
import com.example.Demohs.Exception.ResourceAlreadyExistsException;
import com.example.Demohs.Exception.ResourceNotFoundException;
import com.example.Demohs.Repository.StudentMasterRepository;
import com.example.Demohs.Repository.UserAuthDataRepository;
import com.example.Demohs.Service.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentMasterServiceImpl implements StudentMasterService {

    @Autowired
    private StudentMasterRepository studentMasterRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserAuthDataService userAuthDataService;


    @Autowired
    StudentFeeDetailsService studentFeeDetailsService;
    @Autowired
    ClassFeeStructureService classFeeStructureService;

    @Autowired
    UserAuthDataRepository userAuthDataRepository;

    @Override
    @Transactional
    public String addStudent(StudentMasterDto studentMasterDto) {

        // Check if the student already exists
        Optional<StudentMaster> studentMasterOptional = studentMasterRepository.findByRegNo(studentMasterDto.getRegNo());

        if (studentMasterOptional.isPresent()) {
            throw new ResourceAlreadyExistsException("Student already exists");
        }

        // Capitalize necessary fields
        studentMasterDto.setFirstName(capitalize(studentMasterDto.getFirstName()));
        studentMasterDto.setLastName(capitalize(studentMasterDto.getLastName()));
        studentMasterDto.setFatherName(capitalize(studentMasterDto.getFatherName()));
        studentMasterDto.setMotherName(capitalize(studentMasterDto.getMotherName()));
        studentMasterDto.setAddress(capitalize(studentMasterDto.getAddress()));

        // Create user authentication data
        UserAuthDataDto userAuthDataDto = new UserAuthDataDto();
        userAuthDataDto.setUserName(studentMasterDto.getRegNo());
        userAuthDataDto.setPassword(studentMasterDto.getPassword());
        userAuthDataDto.setRole(studentMasterDto.getRole());
        userAuthDataService.create(userAuthDataDto);

        // Map StudentMasterDto to StudentMaster entity
        StudentMaster studentMaster = modelMapper.map(studentMasterDto, StudentMaster.class);
        studentMaster.setStudentId(UUID.randomUUID());

        // Set the discount at the student level
        studentMaster.setDiscount(studentMasterDto.getDiscount() != null ? studentMasterDto.getDiscount() : 0);

        // Save the student entity
        StudentMaster student = studentMasterRepository.save(studentMaster);

        // Fetch class fee structure
        ClassFeeStructureEntity feeStructure = classFeeStructureService.getFeeStructureByClassName(student.getClassesEntity())
                .orElseThrow(() -> new ResourceNotFoundException("Fee structure not found for class: "
                        + student.getClassesEntity()));

        // Initialize fee details for the student
        studentFeeDetailsService.initializeFeeDetailsForStudent(student, List.of(feeStructure));

        return "Student Created";
    }
    private String capitalize(String input) {
        if (input == null || input.trim().isEmpty()) {
            return input;
        }
        return Arrays.stream(input.split("\\s+"))  // Split words by spaces
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()) // Capitalize first letter
                .collect(Collectors.joining(" "));  // Join words back into a sentence
    }



    @Transactional
    @Override
    public String deleteStudent(String regNo) {
        // Check if the student exists in StudentMaster
        Optional<StudentMaster> studentMasterOptional = studentMasterRepository.findByRegNo(regNo);

        if (studentMasterOptional.isEmpty()) {
            throw new ResourceNotFoundException("Student not found with regNo: " + regNo + " to delete");
        }

        // Delete the student record from StudentMaster
        studentMasterRepository.delete(studentMasterOptional.get());

        // Check if the student exists in UserAuthData
        Optional<UserAuthData> userAuthDataOptional = userAuthDataRepository.findByUsername(regNo);

        if (userAuthDataOptional.isPresent()) {
            // Delete the corresponding UserAuthData record
            userAuthDataRepository.delete(userAuthDataOptional.get());
        }

        return "Deleted successfully";
    }


    @Override
    public StudentMasterDto getStudentByRegNo(String regNo) {
        Optional<StudentMaster> studentMasterOptional = studentMasterRepository.findByRegNo(regNo);

        if(studentMasterOptional.isEmpty())
        {
            throw new ResourceNotFoundException("not exists with this regNo: "+regNo);
        }
        StudentMaster studentMaster = new StudentMaster();

        return modelMapper.map(studentMaster,StudentMasterDto.class);
    }

    @Override
    public List<StudentMasterDto> getAllStudents() {
        List<StudentMaster> studentMasters = studentMasterRepository.findAll();

        List<StudentMasterDto> studentMasterDtoList = new ArrayList<>();
        for (StudentMaster studentMaster : studentMasters) {
            studentMasterDtoList.add(modelMapper.map(studentMaster,StudentMasterDto.class));
        }

        return studentMasterDtoList;
    }

    @Override
    @Transactional
    public String updateStudentById(StudentMasterDto studentMasterDto) {

        // Fetch the existing student record
        StudentMaster studentMaster = studentMasterRepository.findById(studentMasterDto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentMasterDto.getStudentId()));

        // Capture the current username for user authentication data updates
        String currentUsername = studentMaster.getRegNo();

        // Capitalize necessary fields
        studentMasterDto.setFirstName(capitalize(studentMasterDto.getFirstName()));
        studentMasterDto.setLastName(capitalize(studentMasterDto.getLastName()));
        studentMasterDto.setFatherName(capitalize(studentMasterDto.getFatherName()));
        studentMasterDto.setMotherName(capitalize(studentMasterDto.getMotherName()));
        studentMasterDto.setAddress(capitalize(studentMasterDto.getAddress()));

        // Update student details
        studentMaster.setFirstName(studentMasterDto.getFirstName());
        studentMaster.setLastName(studentMasterDto.getLastName());
        studentMaster.setAddress(studentMasterDto.getAddress());
        studentMaster.setPhno(studentMasterDto.getPhno());
        studentMaster.setMotherName(studentMasterDto.getMotherName());
        studentMaster.setFatherName(studentMasterDto.getFatherName());
        studentMaster.setRegNo(studentMasterDto.getRegNo());
        studentMaster.setClassesEntity(studentMasterDto.getClassesEntity());
        studentMaster.setClassSection(studentMasterDto.getClassSection());

        // Update the discount if provided, otherwise leave unchanged
        if (studentMasterDto.getDiscount() != null) {
            studentMaster.setDiscount(studentMasterDto.getDiscount());
        }

        // Save the updated student details
        studentMasterRepository.save(studentMaster);

        // Update user authentication details if the registration number or password has changed
        if (currentUsername != null) {
            System.out.println("Updating UserAuthData for: " + currentUsername);

            UserAuthData userAuthData = userAuthDataService.findByUserName(currentUsername);
            if (userAuthData == null) {
                throw new ResourceNotFoundException("User authentication data not found for username: " + currentUsername);
            }

            // Update username, password, and role if they have changed
            userAuthData.setUsername(studentMasterDto.getRegNo());
            if (studentMasterDto.getPassword() != null) {
                userAuthData.setPassword(studentMasterDto.getPassword());
            }
            userAuthData.setUserRole(studentMasterDto.getRole());

            // Save updated user authentication data
            userAuthDataRepository.save(userAuthData);
        }

        return "Updated successfully";
    }



    @Override
    public StudentMaster findByRegNo(String regNo) {

        Optional<StudentMaster> studentMasterOptional=studentMasterRepository.findByRegNo(regNo);
        if(studentMasterOptional.isEmpty())
        {
            throw new ResourceNotFoundException("Student Not Found"+regNo);
        }
        return studentMasterOptional.get();
    }

//    @Override
//    public List<StudentMasterDto> findByClassesEntityAndClassSection(String classesEntity, String classSection) {
//        // Fetch the list of StudentMaster entities
//        List<StudentMaster> studentMasterList = studentMasterRepository.findByClassesEntityAndClassSection(classesEntity, classSection);
//
//        // Check for null or empty list and return an empty list if needed
//        if (studentMasterList == null || studentMasterList.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        // Use Java Streams to map entities to DTOs
//        return studentMasterList.stream()
//                .map(student -> modelMapper.map(student, StudentMasterDto.class))
//                .collect(Collectors.toList());
//    }

    @Override
    public List<StudentMaster> findByClassesEntityAndClassSection(String classesEntity, String classSection) {
        return studentMasterRepository.findByClassesEntityAndClassSection(classesEntity,classSection);
    }

//    @Override
//    public List<StudentMaster> findByClassesEntityAndClassSectionPagination(String classesEntity, String classSection, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<StudentMaster> studentPage = studentMasterRepository.findByClassesEntityAndClassSection(classesEntity, classSection, pageable);
//        return studentPage.getContent();
//    }

    public Page<StudentMasterDto> findByClassesEntityAndClassSectionPagination(
            String classesEntity, String classSection, int page, int size) {

        // Create pageable object
        Pageable pageable = PageRequest.of(page, size);

        // Fetch paginated StudentMaster data
        Page<StudentMaster> studentPage = studentMasterRepository.findByClassesEntityAndClassSection(classesEntity, classSection, pageable);

        // Map StudentMaster entities to StudentMasterDto with role and password
        Page<StudentMasterDto> studentDtoPage = studentPage.map(student -> {
            // Map basic student details
            StudentMasterDto studentDto = modelMapper.map(student, StudentMasterDto.class);

            // Fetch associated UserAuthData based on regNo
            UserAuthData userAuthData = userAuthDataRepository.findByUsername(student.getRegNo())
                    .orElseThrow(() -> new RuntimeException("UserAuthData not found for regNo: " + student.getRegNo()));

            // Populate role and password
            studentDto.setRole(userAuthData.getStringUserRole());

            studentDto.setPassword(userAuthData.getPassword());

            return studentDto;
        });

        return studentDtoPage;
    }



    @Override
    public Page<StudentMasterDto> getByClassNameAndSection(String classId, String classSection, int page, int size) {
        return null;
    }

    public void updateStudentDiscount(UUID studentId, Integer discount) {
        StudentMaster student = studentMasterRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        student.setDiscount(discount);
        studentMasterRepository.save(student);
    }

//    @Override
//    public Page<StudentMasterDto> getByClassNameAndSection(String classId, String classSection, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<StudentMaster> studentMastersPage = studentMasterRepository.findByClassesEntityAndClassSection(classId, classSection, pageable);
//
//        return studentMastersPage.map(student -> modelMapper.map(student,StudentMasterDto.class));
//    }

}
