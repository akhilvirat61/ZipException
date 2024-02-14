package com.dailycodebuffer.Springboot.tutorial.controller;


import com.dailycodebuffer.Springboot.tutorial.service.VideoStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
public class VideoController {

    @Autowired
    private VideoStorageService videoStorageService;

    @Autowired
    private Environment env;
/*
    @PostMapping("/uploadVideo")
    public String uploadVideo(@RequestParam("file") MultipartFile file) {
        String fileName = videoStorageService.storeVideo(file);
        return "Video uploaded successfully: " + fileName;
    }
*/
@PostMapping("/uploadVideos")
public List<String> uploadVideos(@RequestParam("files") MultipartFile[] files) {
    List<String> fileUrls = new ArrayList<>();
    String port = env.getProperty("server.port");
    for (MultipartFile file : files) {
        String fileName = videoStorageService.storeVideo(file);
        String fileUrl = "http://localhost:"+port+"/videos/" + fileName; // Construct URL
        fileUrls.add(fileUrl);
        //fileNames.add(fileName);

    }
    //return fileNames;
    return fileUrls;
}
    @GetMapping("/videos/{fileName:.+}")
    public ResponseEntity<Resource> downloadVideo(@PathVariable String fileName) {
        // Load file as Resource
        Resource resource = videoStorageService.loadVideoAsResource(fileName);

        // Set content type
        String contentType = "video/mp4";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
