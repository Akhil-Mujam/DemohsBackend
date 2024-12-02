package com.example.Demohs.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class SubjectMarks {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "exam_result_id")
    @JsonBackReference
    private ExamResult examResult;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    @JsonBackReference
    private Subject subject;

    @Column(name = "marks")
    private String marks;

    @Column(name = "grade")
    private String grade;

}
