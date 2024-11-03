package com.example.Demohs.Entity;

import com.example.Demohs.Dto.AllTeachersDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class AllTeachers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teacherId;

    @Column(nullable = false, unique = true)
    private String regNo;  // Teacher registration number

    private String firstName;

    private String lastName;

    private String phno;

    private String address;

    @OneToOne
    TeacherClass teacherClass;


}
