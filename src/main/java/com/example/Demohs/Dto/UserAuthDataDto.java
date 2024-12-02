package com.example.Demohs.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthDataDto {

    @NotNull
    private UUID userID;
    @NotNull
    private String userName;

    @NotNull
    private String password;

    @NotNull
    private String role;
}
