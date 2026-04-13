package com.example.real_time_messaging_system.controller;


import com.example.real_time_messaging_system.repository.ChatRepository;
import com.example.real_time_messaging_system.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chats")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllChats(Authentication authentication) {
        return ResponseEntity.ok(chatService.findAllChats(authentication));
    }
}
