package com.example.real_time_messaging_system.controller;

import com.example.real_time_messaging_system.dto.TypingIndicatorRequest;
import com.example.real_time_messaging_system.dto.TypingIndicatorResponse;
import com.example.real_time_messaging_system.entity.ChatUser;
import com.example.real_time_messaging_system.entity.User;
import com.example.real_time_messaging_system.repository.ChatRepository;
import com.example.real_time_messaging_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping
@RequiredArgsConstructor
public class TypingController {

    private final UserRepository userRepository;
    private SimpMessagingTemplate messagingTemplate;
    private final ChatRepository chatRepository;

    @MessageMapping("/chat.typing")
    public void handleTyping(TypingIndicatorRequest request, Principal principal){
        String email = principal.getName();
        User sender = userRepository.findByEmail(email).orElseThrow();

        TypingIndicatorResponse response = new TypingIndicatorResponse(
                request.chatId(),
                sender.getUserId(),
                request.typing()
        );

        messagingTemplate.convertAndSend("/topic/chat/" + request.chatId() + "/typing", response);

    }


}
