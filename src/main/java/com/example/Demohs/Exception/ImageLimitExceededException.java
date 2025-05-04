package com.example.Demohs.Exception;

// 8. Custom Exception
public class ImageLimitExceededException extends RuntimeException {
    public ImageLimitExceededException(String message) {
        super(message);
    }
}
