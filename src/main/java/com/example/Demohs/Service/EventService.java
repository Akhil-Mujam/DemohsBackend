package com.example.Demohs.Service;

import com.example.Demohs.Dto.EventDto;
import com.example.Demohs.Dto.EventImageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;



public interface EventService {
    EventDto createEvent(EventDto eventDto);

//    List<EventDto> getAllEvents();
    public Page<EventDto> getAllEvents(Pageable pageable);

    EventDto getEventById(UUID id);

    EventDto updateEvent(UUID id, EventDto eventDto);

    void deleteEvent(UUID id);

     List<EventImageDto> uploadImages(UUID eventId, List<MultipartFile> files);

    void deleteImage(UUID imageId);

    Page<EventImageDto> getImagesByEvent(UUID eventId, Pageable pageable);
}