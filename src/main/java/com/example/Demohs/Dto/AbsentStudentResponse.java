package com.example.Demohs.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbsentStudentResponse {

    private String regNo;
    private String firstName;
    private String lastName;
    private String classesEntity;
    private String classSection;
    private String phno;
}

