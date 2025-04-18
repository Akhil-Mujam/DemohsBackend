package com.example.Demohs.Service;

import com.example.Demohs.Dto.StudentMasterDto;
import com.example.Demohs.Entity.StudentMaster;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface StudentMasterService {

    public String addStudent(StudentMasterDto studentMasterDto);

    public String deleteStudent(String regNo);

    public StudentMasterDto getStudentByRegNo(String regNo);

    public List<StudentMasterDto> getAllStudents();

    public String updateStudentById(StudentMasterDto studentMasterDto);

    public StudentMaster findByRegNo(String regNo);

//    public List<StudentMasterDto> getByClassName(String ClassId);

    List<StudentMaster> findByClassesEntityAndClassSection(String classesEntity,String classSection);

    Page<StudentMasterDto> findByClassesEntityAndClassSectionPagination(String classesEntity,String classSection,int page,int size);

    public Page<StudentMasterDto> getByClassNameAndSection(String classId, String classSection, int page, int size);

    public void updateStudentDiscount(UUID studentId, Integer discount);

}
