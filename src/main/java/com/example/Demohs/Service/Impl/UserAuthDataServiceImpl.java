package com.example.Demohs.Service.Impl;

import com.example.Demohs.Dto.UserAuthDataDto;
import com.example.Demohs.Entity.UserAuthData;
import com.example.Demohs.Exception.ResourceAlreadyExistsException;
import com.example.Demohs.Exception.ResourceNotFoundException;
import com.example.Demohs.Repository.UserAuthDataRepository;
import com.example.Demohs.Service.UserAuthDataService;
import com.example.Demohs.util.JavaUtilToken;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserAuthDataServiceImpl implements UserAuthDataService {

    @Autowired
    UserAuthDataRepository userAuthDataRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public UserAuthData findByUserName(String username) {
        Optional<UserAuthData> userAuthData = userAuthDataRepository.findByUsername(username);
        if (userAuthData.isEmpty()) {
            throw new ResourceNotFoundException("User Not found");
        }
        return userAuthData.get();
    }

    @Override
    public String create(UserAuthDataDto userAuthDataDto) {
        Optional<UserAuthData> existingUserAuthData = userAuthDataRepository.findByUsername(userAuthDataDto.getUserName());
        if (existingUserAuthData.isPresent()) {
            throw new ResourceAlreadyExistsException("User Data Already Exists");
        }

        // Generate UUID for the user
        userAuthDataDto.setUserID(UUID.randomUUID());

        // Save the user
        userAuthDataRepository.save(modelMapper.map(userAuthDataDto, UserAuthData.class));
        return "User created successfully";
    }

    @Override
    public String update( UserAuthData userAuthData) {
        userAuthDataRepository.save(userAuthData);
        return "User updated successfully";
    }

    @Override
    public String delete(UUID userId) {
        Optional<UserAuthData> existingUserAuthData = userAuthDataRepository.findById(userId);
        if (existingUserAuthData.isEmpty()) {
            throw new ResourceNotFoundException("User with ID " + userId + " not found");
        }

        userAuthDataRepository.deleteById(userId);
        return "User deleted successfully";
    }

    @Override
    public List<UserAuthData> findAll() {
        return userAuthDataRepository.findAll();
    }
}
