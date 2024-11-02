package com.example.Demohs.Dto;

import com.example.Demohs.Entity.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectMarksDto {
    private String subjectName;
    private String marks;
    private String grade;
}

