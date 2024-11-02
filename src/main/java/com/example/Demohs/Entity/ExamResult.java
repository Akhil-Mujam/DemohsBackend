package com.example.Demohs.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ExamResult {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long examResultId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private StudentMaster studentMaster;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "exam_type_id")
    private ExamType examType;

    @OneToMany(mappedBy = "examResult", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<SubjectMarks> subjectMarks; // Use a list to hold the subject marks

    double total;
}