package com.dailycodebuffer.Springboot.tutorial.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface VideoStorageService {
    String storeVideo(MultipartFile file);
    Resource loadVideoAsResource(String fileName);
}
