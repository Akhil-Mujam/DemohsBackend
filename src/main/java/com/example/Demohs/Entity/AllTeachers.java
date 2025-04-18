package com.example.Demohs.Entity;

import com.example.Demohs.Dto.AllTeachersDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class AllTeachers {

    @Id
    private UUID teacherId;

    @Column(nullable = false, unique = true)
    private String regNo;  // Teacher registration number

    private String firstName;

    private String lastName;

    private String phno;

    private String address;

    private String password;

    private String role;

    @OneToOne(mappedBy = "teacher", cascade = CascadeType.REMOVE) // Remove TeacherClass when AllTeachers is deleted
    @JsonManagedReference  // Prevent infinite recursion by managing this side
    TeacherClass teacherClass;


}
