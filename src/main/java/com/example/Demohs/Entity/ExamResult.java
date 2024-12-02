package com.example.Demohs.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ExamResult {
    @Id
    private UUID examResultId;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "student_id")
    private StudentMaster studentMaster;


    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "exam_type_id")
    private ExamType examType;

    @OneToMany(mappedBy = "examResult")
    @JsonManagedReference
    private List<SubjectMarks> subjectMarks; // Use a list to hold the subject marks

    double total;
}