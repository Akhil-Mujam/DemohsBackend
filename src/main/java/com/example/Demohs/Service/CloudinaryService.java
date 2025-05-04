package com.example.Demohs.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {

    public Map uploadImage(MultipartFile file) throws IOException;
    public void deleteImage(String publicId) throws IOException;

    public String uploadFile(MultipartFile file) throws IOException;
}
