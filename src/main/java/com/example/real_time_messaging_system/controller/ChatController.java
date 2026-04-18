package com.example.real_time_messaging_system.controller;


import com.example.real_time_messaging_system.repository.ChatRepository;
import com.example.real_time_messaging_system.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chats")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllChats(Authentication authentication) {
        return ResponseEntity.ok(chatService.findAllChats(authentication));
    }

    @PostMapping("/read/{chatId}")
    public ResponseEntity<?> readChat(Authentication authentication, @PathVariable("chatId") Long chatId) {
        chatService.markAsRead(authentication.getName(),chatId);
        return ResponseEntity.ok().build();
    }

}
