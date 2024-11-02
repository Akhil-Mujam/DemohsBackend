package com.example.Demohs.Exception;

public class InvalidAttendanceUpdateException extends RuntimeException {
    public InvalidAttendanceUpdateException(String message) {
        super(message);
    }
}
