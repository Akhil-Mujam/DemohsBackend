package com.example.Demohs.Controller;

import com.example.Demohs.Dto.CircularDto;
import com.example.Demohs.Entity.Circular;
import com.example.Demohs.Service.CircularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/circular")
public class CircularController {


    @Autowired
    public CircularService circularService;

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<String> addCircular(@RequestBody CircularDto circularDto) {

        System.out.println("title  = "+circularDto.getTitle());
        System.out.println("drive link = "+circularDto.getDriveLink());
        circularService.addCircular(circularDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Circular added successfully!");
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCircular(@PathVariable UUID id) {
        circularService.deleteCircular(id);
        return ResponseEntity.status(HttpStatus.OK).body("Circular deleted successfully!");
    }

    @GetMapping
    public ResponseEntity<List<Circular>> getAllCirculars() {
        List<Circular> circulars = circularService.getAllCirculars();
        return ResponseEntity.ok(circulars);
    }

}
