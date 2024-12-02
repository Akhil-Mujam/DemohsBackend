package com.example.Demohs.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherClassDto {

    private UUID id;

    // Many-to-one relationship between Teacher and Class

    private   AllTeachersDto teacher;

    private String classEntity;  // Class assigned to the teacher

    private String classSection;

}
