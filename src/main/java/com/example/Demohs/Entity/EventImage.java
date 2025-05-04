package com.example.Demohs.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class EventImage {
    @Id
    @GeneratedValue
    private UUID id;

    private String url;
    private String publicId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;



    // Getters and Setters
}
