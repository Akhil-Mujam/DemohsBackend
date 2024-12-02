package com.example.Demohs.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Attendance {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private String classSection;

    @Column(nullable = false)
    private LocalDate attendanceDate;

    @Column(nullable = false)
    private boolean status;  // true = present, false = absent

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonBackReference
    StudentMaster studentMaster;
    }
