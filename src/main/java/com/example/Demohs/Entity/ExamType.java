package com.example.Demohs.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ExamType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long examId;
    @Column(name = "exam_name")
    String examName;

}
