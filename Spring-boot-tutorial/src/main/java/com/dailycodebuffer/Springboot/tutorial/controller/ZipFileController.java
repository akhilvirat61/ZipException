package com.dailycodebuffer.Springboot.tutorial.controller;

import com.dailycodebuffer.Springboot.tutorial.entity.ZipFile;
import com.dailycodebuffer.Springboot.tutorial.error.FileNotFoundException;
import com.dailycodebuffer.Springboot.tutorial.repository.ZipFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ZipFileController {
    @Autowired
    private ZipFileRepository zipFileRepository;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile[] files) throws IOException {
            List<String> uploadedFiles = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    throw new IllegalArgumentException("File is empty. Please upload a valid file.");
                }
                if (!file.getContentType().equals("application/zip") && !file.getOriginalFilename().endsWith(".zip")) {
                    throw new IllegalArgumentException("Invalid file format. Please upload a ZIP file.");
                }

                ZipFile zipFile = new ZipFile();
                zipFile.setContent(file.getBytes());
                zipFileRepository.save(zipFile);
                uploadedFiles.add(file.getOriginalFilename());
            }
            return "Files uploaded successfully: " + uploadedFiles;

    }

    @GetMapping("/file/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        Optional<ZipFile> zipFileOptional = zipFileRepository.findById(id);
        if (zipFileOptional.isPresent()) {
            ZipFile zipFile = zipFileOptional.get();
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=zipfile.zip")
                    .body(zipFile.getContent());
        } else {
            throw new FileNotFoundException("File with ID " + id + " not found");
        }
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<Object> handleFileNotFoundException(FileNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }
}

