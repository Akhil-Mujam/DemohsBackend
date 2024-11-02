package com.example.Demohs.Controller;

import com.example.Demohs.Dto.StudentMasterDto;
import com.example.Demohs.Entity.StudentMaster;
import com.example.Demohs.Service.StudentMasterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {



    @Autowired
    public StudentMasterService studentMasterService;




    @PostMapping("/add")
    public ResponseEntity<StudentMaster> addStudent(@Valid @RequestBody StudentMasterDto studentMasterDto)
    {
        System.out.println(studentMasterDto.getMotherName());
        System.out.println(studentMasterDto.getFatherName());
        StudentMaster studentMaster = studentMasterService.addStudent(studentMasterDto);
        return new ResponseEntity<>(studentMaster,HttpStatus.OK);
    }



    @GetMapping("/getClassStudents/{classId}/{classSection}")
    public ResponseEntity<Page<StudentMasterDto>> getStudentsByClassNameAndSection(
            @PathVariable String classId,
            @PathVariable String classSection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {

        Page<StudentMasterDto> studentMasterDtoPage = studentMasterService.getByClassNameAndSection(classId, classSection, page, size);
        return new ResponseEntity<>(studentMasterDtoPage, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{regNo}")
    public ResponseEntity<String> deleteByRegNo(@PathVariable String regNo)
    {
        return new ResponseEntity<>(studentMasterService.DeleteStudent(regNo),HttpStatus.OK);
    }

}