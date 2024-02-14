package com.dailycodebuffer.Springboot.tutorial.error;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String message) {
        super(message);
    }
}
