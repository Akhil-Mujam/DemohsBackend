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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/student")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {

    @Autowired
    public StudentMasterService studentMasterService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<String> addStudent(@Valid @RequestBody StudentMasterDto studentMasterDto)
    {
        return new ResponseEntity<>(studentMasterService.addStudent(studentMasterDto),HttpStatus.OK);
    }

//   @GetMapping("/getClassStudents/{classId}/{classSection}")
//   public ResponseEntity<List<StudentMasterDto>> getStudentsByClassNameAndSectionList( @PathVariable String classId, @PathVariable String classSection){
//
//     List<StudentMasterDto> studentMasterList =   studentMasterService.findByClassesEntityAndClassSection(classId, classSection);
//        return new ResponseEntity<>(studentMasterList,HttpStatus.OK);
//    }
@GetMapping("/getClassStudents/{classId}/{classSection}")
public ResponseEntity<List<StudentMaster>> getStudentsByClassNameAndSectionList( @PathVariable String classId, @PathVariable String classSection){

    List<StudentMaster> studentMasterList =   studentMasterService.findByClassesEntityAndClassSection(classId, classSection);
    return new ResponseEntity<>(studentMasterList,HttpStatus.OK);
}
    //    public ResponseEntity<Page<StudentMasterDto>> getStudentsByClassNameAndSection(
//            @PathVariable String classId,
//            @PathVariable String classSection,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "30") int size) {
//
//        Page<StudentMasterDto> studentMasterDtoPage = studentMasterService.getByClassNameAndSection(classId, classSection, page, size);
//        return new ResponseEntity<>(studentMasterDtoPage, HttpStatus.OK);
//    }

    @DeleteMapping("/delete/{regNo}")
    public ResponseEntity<String> deleteByRegNo(@PathVariable String regNo)
    {
        return new ResponseEntity<>(studentMasterService.DeleteStudent(regNo),HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateStudent(@PathVariable UUID id, @RequestBody StudentMasterDto studentMasterDto) {
        studentMasterDto.setStudentId(id); // Ensure the DTO contains the correct ID
        String response = studentMasterService.updateStudentById(studentMasterDto);
        return ResponseEntity.ok(response);
    }


}