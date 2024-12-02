package com.example.Demohs.Service;

import com.example.Demohs.Dto.CircularDto;
import com.example.Demohs.Entity.Circular;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CircularService {

    public List<Circular> getAllCirculars();
    public void deleteCircular(UUID id);
    public void addCircular(CircularDto circularDto);
}
