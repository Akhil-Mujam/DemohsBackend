package com.example.Demohs.Dto;

import com.example.Demohs.Entity.ExamType;
import com.example.Demohs.Entity.StudentMaster;
import com.example.Demohs.Entity.Subject;
import com.example.Demohs.Entity.SubjectMarks;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamResultDto {

    private String regNo;
    private String examName;
    private List<SubjectMarksDto> subjectMarksList;
    private double total;

}
