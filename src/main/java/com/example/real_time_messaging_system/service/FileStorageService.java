package com.example.real_time_messaging_system.service;

import com.example.real_time_messaging_system.dto.request.MessageRequest;
import com.example.real_time_messaging_system.entity.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadDir = Paths.get("uploads");
    private final Set<String> allowedFileExtensions = Set.of("image/jpeg", "image/png", "image/gif", "image/webp", "image/bmp");

    public String uploadFile(MultipartFile file) {
       try {
           if (file.isEmpty()) {
               throw new RuntimeException("file is empty");
           }
           if (!allowedFileExtensions.contains(file.getContentType())) {
               throw new RuntimeException("file type not supported");
           }

           if (Files.notExists(uploadDir)) {
               Files.createDirectories(uploadDir);
           }
           long maxSize = 5 * 1024 * 1024; if (file.getSize() > maxSize) { throw new RuntimeException( "File too large" ); }

           String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
           String fileName = UUID.randomUUID() + "." + extension;
           Path target = uploadDir.resolve(fileName);

           Files.copy(file.getInputStream(), target , StandardCopyOption.REPLACE_EXISTING);
           return "/uploads/" + fileName;

       } catch (IOException e) {
           throw new RuntimeException(e);
       }
    }

    public void validateMessage(MessageRequest request) {
        if (request.messageType() == MessageType.TEXT && (request.content() == null || request.content().isBlank())) {
            throw new RuntimeException("message content is empty");
        }
        if (request.messageType() == MessageType.IMAGE && (request.mediaUrl() == null || request.mediaUrl().isBlank())) {
            throw new RuntimeException("Image Require Media");
        }
    }


}
