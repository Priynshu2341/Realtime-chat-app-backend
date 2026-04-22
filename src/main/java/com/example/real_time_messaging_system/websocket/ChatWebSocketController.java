package com.example.real_time_messaging_system.websocket;

import com.example.real_time_messaging_system.dto.ChatOpenRequest;
import com.example.real_time_messaging_system.repository.UserRepository;
import com.example.real_time_messaging_system.service.ChatService;
import com.example.real_time_messaging_system.service.ChatSessionService;
import com.example.real_time_messaging_system.service.MessageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {


    private final ChatSessionService chatSessionService;
    private final UserRepository userRepository;
    private final MessageService messageService;
    private final ChatService chatService;


    @MessageMapping("/chat.open")
    public void openChat(ChatOpenRequest request, Authentication authentication){
        String email =  authentication.getName();
        var user = userRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("User not found"));
        Long userId = user.getUserId();
        Long chatId = request.chatId();
        chatSessionService.setActiveChats(userId, chatId);
        messageService.markAsSeen(chatId,userId);
        chatService.markAsRead(email,chatId);
    }

    @MessageMapping("/chat.close")
    public void closeChat(Authentication authentication){
        String email =  authentication.getName();
        var user =  userRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("User not found"));
        chatSessionService.clearActiveChats(user.getUserId());
    }

}
