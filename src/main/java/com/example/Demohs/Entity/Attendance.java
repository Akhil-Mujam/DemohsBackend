package com.example.Demohs.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_regNo", referencedColumnName = "regNo")
    private StudentMaster studentMaster;


    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private String classSection;

    @Column(nullable = false)
    private LocalDate attendanceDate;

    @Column(nullable = false)
    private boolean status;  // true = present, false = absent

    // Getters and Setters

    }
