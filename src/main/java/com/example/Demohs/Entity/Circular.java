package com.example.Demohs.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class Circular {
    @Id
    private UUID id;
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String driveLink;
    private LocalDateTime createdDate;
}
