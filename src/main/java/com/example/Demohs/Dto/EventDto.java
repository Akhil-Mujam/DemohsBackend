package com.example.Demohs.Dto;

import com.example.Demohs.Entity.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private UUID id;
    private String name;
    private String description;
    private LocalDate eventDate;
    private String thumbnailUrl;
    private List<EventImageDto> images;

    public static EventDto fromEntity(Event event, boolean includeImages) {
        EventDto dto = new EventDto();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate());
        dto.setThumbnailUrl(event.getThumbnailUrl());

        if (includeImages && event.getImages() != null) {
            dto.setImages(event.getImages().stream()
                    .map(image -> {
                        EventImageDto imageDto = new EventImageDto();
                        imageDto.setId(image.getId());
                        imageDto.setUrl(image.getUrl()); // Adjust if your field is named differently
                        return imageDto;
                    })
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
