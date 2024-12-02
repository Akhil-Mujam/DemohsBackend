package com.example.Demohs.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthData {
    @Id
    private UUID userID;

    @Column(name = "user_name")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "user_role")
    private String userRole; // Store as comma-separated string

    public List<String> getUserRole() {
        String[] roles = userRole.replace("\"", "").split(",");
        return Arrays.asList(roles);
    }

    public String getStringUserRole()
    {
        return userRole;
    }
}
