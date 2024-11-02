package com.example.Demohs.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class StudentMaster {

    @Id
    @Column(nullable = false, unique = true)
    private String regNo;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String fatherName;

    @Column(nullable = false)
    private String motherName;

    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
    @Column(nullable = false)
    private String phno;

    @Column(nullable = false)
    private String address;

    // Many-to-one relationship with ClassData
    private String classesEntity;  // Corrected field name

    private String classSection;

    @OneToMany
    List<ExamResult> examResultList;

}
