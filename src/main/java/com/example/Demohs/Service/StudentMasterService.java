package com.example.Demohs.Service;

import com.example.Demohs.Dto.StudentMasterDto;
import com.example.Demohs.Entity.StudentMaster;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface StudentMasterService {

    public StudentMaster addStudent(StudentMasterDto studentMasterDto);

    public String DeleteStudent(String regNo);

    public StudentMasterDto getStudentByRegNo(String regNo);

    public List<StudentMasterDto> getAllStudents();

    public StudentMaster findByRegNo(String regNo);

//    public List<StudentMasterDto> getByClassName(String ClassId);

    public Page<StudentMasterDto> getByClassNameAndSection(String classId, String classSection, int page, int size);

}
