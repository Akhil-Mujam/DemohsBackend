package com.example.Demohs.Dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStudentDiscountRequest {
    @NotNull(message = "Student ID cannot be null")
    private UUID studentId;

    @Min(value = 0, message = "Discount must be non-negative")
    private Integer discount;
}

