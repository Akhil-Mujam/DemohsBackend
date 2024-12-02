package com.example.Demohs.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CircularDto {


    private UUID id;
    @NotNull(message = "Title cannot be null")
    private String title;
    @NotNull(message = "Drive link cannot be null")
    private String driveLink;
    private LocalDateTime createdDate;

}
