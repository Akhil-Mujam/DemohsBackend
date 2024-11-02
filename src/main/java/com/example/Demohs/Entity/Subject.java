package com.example.Demohs.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.ConditionalOnGraphQlSchema;
import org.springframework.stereotype.Component;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long subjectId;

    @Column(name = "subject_name")
    String subjectName;

}
