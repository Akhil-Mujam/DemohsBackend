package com.example.Demohs.Entity;

import com.example.Demohs.Dto.StudentDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long Id;

    public String email;

    @Override
    public String toString() {
        return "Student{" +
                "Id=" + Id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String password;


    public StudentDto toStudentDto(Student student)
    {
        StudentDto stu = new StudentDto();
        stu.email=student.email;
        stu.password=student.password;
        stu.Id= student.Id;
        return stu;
    }
}
