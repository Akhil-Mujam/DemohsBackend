package com.example.Demohs.Controller;

import com.example.Demohs.Entity.EventEntity;
import com.example.Demohs.Service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadEvent(
            @RequestParam String eventName,
            @RequestParam String eventDescription,
            @RequestParam MultipartFile[] images) {
        try {
            eventService.uploadEvent(eventName, eventDescription, images);
            return ResponseEntity.ok("Event uploaded successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading event: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<EventEntity>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }
}

