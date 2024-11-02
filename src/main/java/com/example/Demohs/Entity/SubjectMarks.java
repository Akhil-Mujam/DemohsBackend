package com.example.Demohs.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class SubjectMarks {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_result_id")
    @JsonBackReference
    private ExamResult examResult;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(name = "marks")
    private String marks;

    @Column(name = "grade")
    private String grade;

}
