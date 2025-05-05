package com.example.Demohs.Controller;

import com.example.Demohs.Dto.EventDto;
import com.example.Demohs.Dto.EventImageDto;
import com.example.Demohs.Service.CloudinaryService;
import com.example.Demohs.Service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final CloudinaryService cloudinaryService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventDto> createEvent(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("eventDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate eventDate,
            @RequestParam("thumbnail") MultipartFile thumbnail
    ) {
        try {

            System.out.println("---------------------Create Event Called--------------------");
            String thumbnailUrl = cloudinaryService.uploadFile(thumbnail);

            EventDto dto = new EventDto();
            dto.setName(name);
            dto.setDescription(description);
            dto.setEventDate(eventDate);
            dto.setThumbnailUrl(thumbnailUrl);

            EventDto savedEvent = eventService.createEvent(dto);
            return ResponseEntity.ok(savedEvent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping
    public Page<EventDto> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        System.out.println("---------------------Get Event Called--------------------");
        return eventService.getAllEvents(PageRequest.of(page, size, Sort.by("eventDate").descending()));
    }

//    @GetMapping
//    public ResponseEntity<List<EventDto>> getAllEvents() {
//        return ResponseEntity.ok(eventService.getAllEvents());
//    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable UUID id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable UUID id, @RequestBody EventDto eventDto) {
        return ResponseEntity.ok(eventService.updateEvent(id, eventDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable UUID id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok("Event and associated images deleted");
    }

    @PostMapping("/{eventId}/images")
    public ResponseEntity<List<EventImageDto>> uploadImages(
            @PathVariable UUID eventId,
            @RequestParam("files") List<MultipartFile> files) {
        System.out.println("---------------------List Get Event Called--------------------");
        return ResponseEntity.ok(eventService.uploadImages(eventId, files));
    }

    @DeleteMapping("/images/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable UUID id) {
        eventService.deleteImage(id);
        return ResponseEntity.ok("Image deleted successfully");
    }
}