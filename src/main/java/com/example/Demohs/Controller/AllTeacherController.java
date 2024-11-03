package com.example.Demohs.Controller;

import com.example.Demohs.Dto.AllTeachersDto;
import com.example.Demohs.Entity.AllTeachers;
import com.example.Demohs.Repository.ClassTeacherRepository;
import com.example.Demohs.Service.AllTeachersService;
import com.example.Demohs.Service.ClassTeacherService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<AllTeachersDto>> getAllTeachers()
    {
        List<AllTeachersDto> allTeachersDtoList = allTeachersService.getAllTeachers();

        return new ResponseEntity<>(allTeachersDtoList,HttpStatus.OK);
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
}
