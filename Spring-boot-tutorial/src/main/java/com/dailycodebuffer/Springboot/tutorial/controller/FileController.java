package com.dailycodebuffer.Springboot.tutorial.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
    
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FileController {

    private static final String VIDEO_FOLDER_PATH = "D:\\react\\React_Videos\\";

    @GetMapping("/videos")
    public ResponseEntity<List<String>> getVideoList() {
        File videoFolder = new File(VIDEO_FOLDER_PATH);
        if (!videoFolder.exists() || !videoFolder.isDirectory()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        File[] videoFiles = videoFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp4"));

        if (videoFiles == null || videoFiles.length == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<String> videoNames = new ArrayList<>();
        for (File file : videoFiles) {
            videoNames.add(file.getName());
        }

        return new ResponseEntity<>(videoNames, HttpStatus.OK);
    }

    @GetMapping("/videos/download")
    public ResponseEntity<InputStreamResource> downloadVideo(@RequestParam("filename") String filename) {
        try {
            File file = new File(VIDEO_FOLDER_PATH + File.separator + filename);

            if (!file.exists()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .body(resource);

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

