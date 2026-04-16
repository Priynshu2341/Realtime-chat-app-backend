package com.example.real_time_messaging_system.controller;

import com.example.real_time_messaging_system.dto.BasicMessageResponse;
import com.example.real_time_messaging_system.dto.MessageRequest;
import com.example.real_time_messaging_system.dto.MessageResponse;
import com.example.real_time_messaging_system.entity.Chat;
import com.example.real_time_messaging_system.entity.Message;
import com.example.real_time_messaging_system.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.time.LocalDateTime;
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
    public ResponseEntity<?> findMessagePaginated(
            @PathVariable String chatKey,
            Authentication authentication,
            @RequestParam(required = false) String cursor
    ) throws AccessDeniedException {

        String email = authentication.getName();
        LocalDateTime parsedCursor = (cursor==null) ? null : LocalDateTime.parse(cursor);
        return ResponseEntity.ok(messageService.findAllMessagesInChat(email,chatKey,parsedCursor));
    }




    @MessageMapping("/chat.send")
    public void sendMessageWebsocket(MessageRequest messageRequest,Principal principal){
        if (principal == null){
            System.out.println("principal is null");
            return;
        }
        String email = principal.getName();
        MessageResponse messageResponse = messageService.saveMessageFromSocket(email,messageRequest);
        simpMessagingTemplate.convertAndSend("/topic/chat/" + messageResponse.chatId(),messageResponse);
    }


}
