package com.example.Demohs.Service.Impl;

import com.example.Demohs.Dto.CircularDto;
import com.example.Demohs.Entity.Circular;
import com.example.Demohs.Exception.ResourceNotFoundException;
import com.example.Demohs.Repository.CircularRepository;
import com.example.Demohs.Service.CircularService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CircularServiceImpl implements CircularService {

    @Autowired
    private CircularRepository circularRepository;
    @Autowired
    ModelMapper modelMapper;


    public void deleteCircular(UUID id) {
        if (!circularRepository.existsById(id)) {
            throw new ResourceNotFoundException("Circular with ID " + id + " not found");
        }
        circularRepository.deleteById(id);
    }

    public void addCircular(CircularDto circularDto) {


        Circular circular = modelMapper.map(circularDto,Circular.class);
        System.out.println("service title "+circular.getTitle());
        System.out.println("drive link service = "+circular.getDriveLink());
        circular.setId(UUID.randomUUID());
        circular.setCreatedDate(LocalDateTime.now());
        circularRepository.save(circular);
    }

    @Override
    public List<Circular> getAllCirculars() {
        return circularRepository.findAll();
    }


}
