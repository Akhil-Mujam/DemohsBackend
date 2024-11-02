package com.example.Demohs.Dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentMasterDto
{
    private String regNo;

    private String firstName;

    private String lastName;

    private String fatherName;

    private String motherName;

    @Size(min = 10, max = 10, message = "Phone number must be exactly 10 characters long")
    private String phno;

    private String address;

    private String classesEntity;

    private String classSection;


}
