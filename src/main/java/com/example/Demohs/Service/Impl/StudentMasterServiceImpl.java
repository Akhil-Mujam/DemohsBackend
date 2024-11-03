package com.example.Demohs.Service.Impl;

import com.example.Demohs.Dto.StudentMasterDto;
import com.example.Demohs.Entity.StudentMaster;
import com.example.Demohs.Exception.ResourceAlreadyExistsException;
import com.example.Demohs.Exception.ResourceNotFoundException;
import com.example.Demohs.Repository.StudentMasterRepository;
import com.example.Demohs.Service.StudentMasterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentMasterServiceImpl implements StudentMasterService {

    @Autowired
    private StudentMasterRepository studentMasterRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public StudentMaster addStudent(StudentMasterDto studentMasterDto) {

         Optional<StudentMaster> studentMasterOptional = studentMasterRepository.findByRegNo(studentMasterDto.getRegNo());

         if(studentMasterOptional.isPresent())
         {
             throw new ResourceAlreadyExistsException("already exists");
         }
         StudentMaster studentMaster =modelMapper.map(studentMasterDto, StudentMaster.class);
        return studentMasterRepository.save(studentMaster);
    }

    @Override
    public String DeleteStudent(String regNo) {
        Optional<StudentMaster> studentMasterOptional = studentMasterRepository.findByRegNo(regNo);

        if(studentMasterOptional.isEmpty())
        {
            throw new ResourceNotFoundException("not exists with this regNo:"+regNo+" to Delete");
        }

        studentMasterRepository.delete(studentMasterOptional.get());

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
    public StudentMaster findByRegNo(String regNo) {

        Optional<StudentMaster> studentMasterOptional=studentMasterRepository.findByRegNo(regNo);
        if(studentMasterOptional.isEmpty())
        {
            throw new ResourceNotFoundException("Student Not Found"+regNo);
        }
        return studentMasterOptional.get();
    }

    @Override
    public List<StudentMaster> findByClassesEntityAndClassSection(String classesEntity, String classSection) {
        return studentMasterRepository.findByClassesEntityAndClassSection(classesEntity,classSection);
    }

    @Override
    public Page<StudentMasterDto> getByClassNameAndSection(String classId, String classSection, int page, int size) {
        return null;
    }

//    @Override
//    public Page<StudentMasterDto> getByClassNameAndSection(String classId, String classSection, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<StudentMaster> studentMastersPage = studentMasterRepository.findByClassesEntityAndClassSection(classId, classSection, pageable);
//
//        return studentMastersPage.map(student -> modelMapper.map(student,StudentMasterDto.class));
//    }

}
