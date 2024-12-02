package com.example.Demohs.Dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Data
@Component
public class SubjectDto {

    UUID id;

    String subjectName;

}
