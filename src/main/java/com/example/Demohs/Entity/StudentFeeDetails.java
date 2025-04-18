package com.example.Demohs.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentFeeDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "student_id", nullable = false)
    private StudentMaster studentMaster;

    @Column(nullable = false)
    private String termName; // Example: "1st Term", "2nd Term", etc.

    private Integer amount; // Fee for this term

    private Boolean isPaid = false;

    private LocalDate paidDate;

}
