package com.example.Demohs.Repository;

import com.example.Demohs.Entity.Event;
import com.example.Demohs.Entity.EventImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventImageRepository extends JpaRepository<EventImage, UUID> {
    int countByEventId(UUID eventId);
    Page<EventImage> findAllByEventId(UUID eventId, Pageable pageable);
    Page<EventImage> findByEvent(Event event, Pageable pageable);
    long countByEvent(Event event);


}
