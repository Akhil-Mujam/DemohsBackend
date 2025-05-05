package com.example.Demohs.Service.Impl;

import com.example.Demohs.Dto.EventDto;
import com.example.Demohs.Dto.EventImageDto;
import com.example.Demohs.Entity.Event;
import com.example.Demohs.Entity.EventImage;
import com.example.Demohs.Exception.ImageLimitExceededException;
import com.example.Demohs.Exception.ResourceNotFoundException;
import com.example.Demohs.Repository.EventImageRepository;
import com.example.Demohs.Repository.EventRepository;
import com.example.Demohs.Service.CloudinaryService;
import com.example.Demohs.Service.EventService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventImageRepository imageRepository;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper mapper;
    private static final int MAX_IMAGES_PER_EVENT = 20;

    @Override
    public EventDto createEvent(EventDto eventDto) {
        Event entity = mapper.map(eventDto, Event.class);
        entity.setId(UUID.randomUUID());
        return mapper.map(eventRepository.save(entity), EventDto.class);
    }


//    public List<EventDto> getAllEvents() {
//        return eventRepository.findAll()
//                .stream()
//                .map(e -> mapper.map(e, EventDto.class))
//                .collect(Collectors.toList());
//    }

    @Override
    public Page<EventDto> getAllEvents(Pageable pageable) {
        System.out.println("---------------------Inside Get Event Called--------------------");
        return eventRepository.findAll(pageable)
                .map(event -> EventDto.fromEntity(event, false));
    }



    @Override
    public EventDto getEventById(UUID id) {
        Event entity = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        return mapper.map(entity, EventDto.class);
    }

    @Override
    public EventDto updateEvent(UUID id, EventDto eventDto) {
        Event entity = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        entity.setName(eventDto.getName());
        entity.setDescription(eventDto.getDescription());
        entity.setEventDate(eventDto.getEventDate());
        return mapper.map(eventRepository.save(entity), EventDto.class);
    }

    @Override
    public void deleteEvent(UUID id) {
        Event entity = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        entity.getImages().forEach(img -> {
            try {
                cloudinaryService.deleteImage(img.getPublicId());
            } catch (IOException ignored) {}
        });
        eventRepository.delete(entity);
    }

    @Override
    public List<EventImageDto> uploadImages(UUID eventId, List<MultipartFile> files) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

        // Optional: Check existing image count

        long existingCount = imageRepository.countByEvent(event);
        if (existingCount + files.size() > MAX_IMAGES_PER_EVENT) {
            throw new ImageLimitExceededException("Cannot upload more than " + MAX_IMAGES_PER_EVENT + " images for this event.");
        }

        List<EventImageDto> uploadedImages = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                Map<?, ?> uploadResult = cloudinaryService.uploadImage(file);
                EventImage image = new EventImage();

                image.setId(UUID.randomUUID());
                image.setUrl((String) uploadResult.get("secure_url"));
                image.setPublicId((String) uploadResult.get("public_id"));
                image.setEvent(event);

                uploadedImages.add(mapper.map(imageRepository.save(image), EventImageDto.class));
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image: " + file.getOriginalFilename(), e);
            }
        }

        return uploadedImages;
    }

    @Override
    public void deleteImage(UUID imageId) {
        EventImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));
        try {
            cloudinaryService.deleteImage(image.getPublicId());
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image from cloud: " + e.getMessage());
        }
        imageRepository.delete(image);
    }

    @Override
    public Page<EventImageDto> getImagesByEvent(UUID eventId, Pageable pageable) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

        return imageRepository.findByEvent(event, pageable)
                .map(image -> mapper.map(image, EventImageDto.class));
    }
}

