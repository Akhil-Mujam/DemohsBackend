package com.example.Demohs.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "TeacherClass")
public class TeacherClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "teacherId")
    @JsonBackReference
    private AllTeachers teacher;  // Referencing the AllTeachers entity

    @Column(nullable = false)
    private String classEntity;

    private String classSection;

}
