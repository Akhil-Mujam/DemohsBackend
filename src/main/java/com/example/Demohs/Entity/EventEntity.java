package com.example.Demohs.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String eventName;
    private String eventDescription;

//    @Temporal(TemporalType.DATE)
//    private Date eventDate;

    @ElementCollection
    private List<String> imagePaths = new ArrayList<>();

    // Getters and setters
}

