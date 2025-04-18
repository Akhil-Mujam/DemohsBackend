package com.example.Demohs.Controller;

import com.example.Demohs.Entity.ClassFeeStructureEntity;
import com.example.Demohs.Service.ClassFeeStructureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/class-fees")
@RequiredArgsConstructor
public class ClassFeeStructureController {

    private final ClassFeeStructureService classFeeStructureService;

    // Create or update a class fee structure
    @PostMapping("/create")
    public ResponseEntity<ClassFeeStructureEntity> createOrUpdateFeeStructure(@RequestBody ClassFeeStructureEntity feeStructure) {
        ClassFeeStructureEntity updatedStructure = classFeeStructureService.createOrUpdateFeeStructure(feeStructure);
        return ResponseEntity.ok(updatedStructure);
    }

    // Get fee structure by class name
    @GetMapping("/{className}")
    public ResponseEntity<ClassFeeStructureEntity> getFeeStructureByClassName(@PathVariable String className) {
        Optional<ClassFeeStructureEntity> feeStructure = classFeeStructureService.getFeeStructureByClassName(className);
        return feeStructure.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<ClassFeeStructureEntity>> getAllClassFeeStructures()
    {
        List<ClassFeeStructureEntity> classFeeStructureEntityList = classFeeStructureService.getAllClassesFeeStrutures();

        return new ResponseEntity<>(classFeeStructureEntityList, HttpStatus.OK);
    }

    // Update term fees
    @PutMapping("/{className}/update-fees")
    public ResponseEntity<ClassFeeStructureEntity> updateTermFees(
            @PathVariable String className,
            @RequestParam Integer term1Fee,
            @RequestParam Integer term2Fee,
            @RequestParam Integer term3Fee) {
        ClassFeeStructureEntity updatedStructure = classFeeStructureService.updateTermFees(className, term1Fee, term2Fee, term3Fee);
        return ResponseEntity.ok(updatedStructure);
    }
}
