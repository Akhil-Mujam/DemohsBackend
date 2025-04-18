package com.example.Demohs.Service;

import com.example.Demohs.Entity.EventEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EventService {

    public EventEntity uploadEvent(String eventName, String eventDescription, MultipartFile[] images) throws IOException;

    public List<EventEntity> getAllEvents();
}
