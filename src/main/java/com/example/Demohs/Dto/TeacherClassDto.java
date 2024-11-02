package com.example.Demohs.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherClassDto {

    private Long id;

    // Many-to-one relationship between Teacher and Class

    private   AllTeachersDto teacher;

    private String classEntity;  // Class assigned to the teacher

    private String classSection;

}
