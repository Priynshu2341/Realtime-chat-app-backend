package com.example.real_time_messaging_system.controller;

import com.example.real_time_messaging_system.dto.response.FileUploadResponse;
import com.example.real_time_messaging_system.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/uploads")
public class MediaUploadController {

    private final FileStorageService fileStorageService;

    @PostMapping(value = "/file",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FileUploadResponse upload(@RequestParam("file") MultipartFile file) {
       String mediaUrl = fileStorageService.uploadFile(file);
       return new FileUploadResponse(mediaUrl);
    }
}
