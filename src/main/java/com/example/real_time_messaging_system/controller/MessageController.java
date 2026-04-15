package com.example.real_time_messaging_system.controller;

import com.example.real_time_messaging_system.dto.BasicMessageResponse;
import com.example.real_time_messaging_system.dto.MessageRequest;
import com.example.real_time_messaging_system.dto.MessageResponse;
import com.example.real_time_messaging_system.entity.Message;
import com.example.real_time_messaging_system.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/message")
@RequiredArgsConstructor
public class MessageController {


    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/send")
    public MessageResponse sendMessage(Authentication authentication, @RequestBody MessageRequest messageRequest){
        return messageService.saveMessages(messageRequest,authentication);

    }

    @GetMapping("/find/{chatKey}")
    public List<BasicMessageResponse> findAllMessagesInChat(@PathVariable String chatKey){
        return messageService.findAllMessagesInChat(chatKey);

    }

    @MessageMapping("/chat.send")
    public void sendMessageWebsocket(MessageRequest messageRequest, Authentication authentication){
        String email = authentication.getName();
        MessageResponse messageResponse = messageService.saveMessageFromSocket(email,messageRequest);
        simpMessagingTemplate.convertAndSend("/topic/chat/" + messageResponse.chatId(),messageResponse);
    }


}
