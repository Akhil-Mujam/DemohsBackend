package com.example.Demohs.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFeeDetailRequest {
    private String termName;
    private LocalDate paidDate;
    private Integer discount;
    private Boolean isPaid;
}

