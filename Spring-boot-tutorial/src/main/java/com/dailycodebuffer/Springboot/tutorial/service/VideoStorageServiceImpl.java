package com.dailycodebuffer.Springboot.tutorial.service;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class VideoStorageServiceImpl implements VideoStorageService {

    private final Path videoStorageLocation = Paths.get("uploads");

    @Override
    public String storeVideo(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Invalid file name");
            }

            Path targetLocation = this.videoStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to store video: " + ex.getMessage());
        }
    }



    @Override
    public Resource loadVideoAsResource(String fileName) {
        try {
            Path filePath = this.videoStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("Video not found: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Video not found: " + fileName);
        }
    }
}