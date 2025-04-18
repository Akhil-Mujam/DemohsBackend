package com.example.Demohs.Service.Impl;

import com.example.Demohs.Entity.EventEntity;
import com.example.Demohs.Repository.EventRepository;
import com.example.Demohs.Service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
public class EventServiceImpl implements EventService {

    @Value("${event.images.upload-dir}")
    private String uploadDir;


    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public EventEntity uploadEvent(String eventName, String eventDescription, MultipartFile[] images) throws IOException {
        // Save event metadata
        EventEntity event = new EventEntity();
        event.setEventName(eventName);
        event.setEventDescription(eventDescription);

        // Construct the directory path for the event
        String eventPath = uploadDir + "/" + eventName;
        File directory = new File(eventPath);

        // Ensure the directory exists
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new IOException("Failed to create directory: " + eventPath);
            }
        }

        // Save image files
        List<String> imagePaths = new ArrayList<>();
        for (MultipartFile image : images) {
            // Validate and process the image file
            String originalFilename = image.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new IllegalArgumentException("One of the uploaded files has an invalid filename.");
            }

            // Generate a unique name for the file
            String imageName = UUID.randomUUID() + "_" + originalFilename;

            // Save the image to the event directory
            File imageFile = new File(directory, imageName);
            image.transferTo(imageFile);

            // Add the image path to the list (relative path for frontend usage)
            imagePaths.add("/events/" + eventName + "/" + imageName);
        }

        // Set the image paths to the event entity
        event.setImagePaths(imagePaths);

        // Save event to the database
        return eventRepository.save(event);
    }


    @Override
    public List<EventEntity> getAllEvents() {
        return eventRepository.findAll();
    }

}
