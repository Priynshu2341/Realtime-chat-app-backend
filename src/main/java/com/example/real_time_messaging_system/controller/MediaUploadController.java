package com.example.real_time_messaging_system.controller;

import com.example.real_time_messaging_system.dto.response.FileUploadResponse;
import com.example.real_time_messaging_system.service.FileStorageService;
import com.example.real_time_messaging_system.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/uploads")
public class MediaUploadController {

    private final S3Service s3Service;

    @PostMapping(value = "/file",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FileUploadResponse upload(@RequestParam("file") MultipartFile file) throws IOException {
       String mediaUrl = s3Service.fileUpload(file);
       return new FileUploadResponse(mediaUrl);
    }
}
