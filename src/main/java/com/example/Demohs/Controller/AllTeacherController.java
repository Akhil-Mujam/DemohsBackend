package com.example.Demohs.Controller;

import com.example.Demohs.Dto.AllTeachersDto;
import com.example.Demohs.Entity.AllTeachers;
import com.example.Demohs.Repository.ClassTeacherRepository;
import com.example.Demohs.Service.AllTeachersService;
import com.example.Demohs.Service.ClassTeacherService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/teacher")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AllTeacherController {

    @Autowired
    public AllTeachersService allTeachersService;

    @Autowired
    private ClassTeacherService classTeacherService;

    @PostMapping("/add")
    public ResponseEntity<String> addTeacher(@Valid @RequestBody AllTeachersDto allTeachersDto)
    {
        return new ResponseEntity<>(allTeachersService.addTeacher(allTeachersDto),HttpStatus.OK );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTeacher(@PathVariable UUID id, @RequestBody AllTeachersDto allTeachersDto)
    {
        allTeachersDto.setTeacherId(id);
        return new ResponseEntity<>(allTeachersService.updateTeacher(allTeachersDto),HttpStatus.OK );
    }

    @GetMapping("/{regNo}")
    public ResponseEntity<AllTeachers> getTeacher(@PathVariable String regNo)
    {
        return new ResponseEntity<>(allTeachersService.getTeacher(regNo),HttpStatus.OK);
    }

    @DeleteMapping("/delete/{regNo}")
    public ResponseEntity<String> deleteTeacher(@PathVariable String regNo)
    {
        return  new ResponseEntity<>(allTeachersService.deleteTeacher(regNo),HttpStatus.OK);
    }
    @GetMapping("/all")
    public Page<AllTeachersDto> getAllTeachers(
            @RequestParam(defaultValue = "0") int page, // Default page number
            @RequestParam(defaultValue = "10") int size // Default page size
    ) {
        return allTeachersService.getAllTeachers(page, size);
    }

    @PostMapping("/assign")
    public ResponseEntity<String> assignClassTeacher(@RequestParam String regNo,@RequestParam String classEntity,@RequestParam String classSection)
    {

       return  new ResponseEntity<>(classTeacherService.makeClassTeacher(regNo,classEntity,classSection),HttpStatus.OK);
    }

    @GetMapping("/getTeacher/{classEntity}")
    public ResponseEntity<AllTeachersDto> getTeacherByClassName(@PathVariable String classEntity)
    {
        return new ResponseEntity<>(classTeacherService.getTeacherByClassName(classEntity),HttpStatus.OK);
    }


    @GetMapping("/class-details/{regNo}")
    public ResponseEntity<Map<String, String>> getClassDetails(@PathVariable String regNo) {
        Map<String, String> classDetails = classTeacherService.getClassDetailsByRegNo(regNo);
        return ResponseEntity.ok(classDetails);
    }

}
