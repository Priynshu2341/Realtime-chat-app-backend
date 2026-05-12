package com.example.real_time_messaging_system.controller;

import com.example.real_time_messaging_system.dto.*;
import com.example.real_time_messaging_system.dto.request.MessageRequest;
import com.example.real_time_messaging_system.dto.response.MessageResponse;
import com.example.real_time_messaging_system.entity.User;
import com.example.real_time_messaging_system.repository.UserRepository;
import com.example.real_time_messaging_system.service.MessageService;
import com.example.real_time_messaging_system.websocket.PresenceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/v1/message")
@RequiredArgsConstructor
public class MessageController {


    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserRepository userRepository;
    private final PresenceService presenceService;

    @PostMapping("/send")
    public MessageResponse sendMessage(Authentication authentication, @RequestBody MessageRequest messageRequest){
        return messageService.saveMessages(messageRequest,authentication);

    }

    @GetMapping("/find/{chatKey}")
    public ResponseEntity<?> findMessagePaginated(
            @PathVariable String chatKey,
            Authentication authentication,
            @RequestParam(required = false) String cursorTime,
            @RequestParam(required = false) Long cursorId
    ) throws AccessDeniedException {

        String email = authentication.getName();
        MessageCursor cursor = null;
        if (cursorTime != null && cursorId != null ) {
            cursor = new MessageCursor(
                    LocalDateTime.parse(cursorTime),
                    cursorId
            );
        }
        return ResponseEntity.ok(messageService.findAllMessagesInChat(email,chatKey,cursor));
    }




    @MessageMapping("/chat.send")
    public void sendMessageWebsocket(MessageRequest messageRequest,Principal principal){
        if (principal == null){
            System.out.println("principal is null");
            return;
        }
        String email = principal.getName();
        Long receiverId = messageRequest.receiverId();
        User receiver = userRepository.findById(receiverId).orElseThrow(()->new EntityNotFoundException("Receiver not found"));
        boolean isOnline = presenceService.isUserOnline(receiver.getEmail());


        MessageResponse messageResponse = messageService.saveMessageFromSocket(email,messageRequest,isOnline);
        //send to chat
        simpMessagingTemplate.convertAndSend("/topic/chat/" + messageResponse.chatId(),messageResponse);
        //send to receiver message
        simpMessagingTemplate.convertAndSendToUser(receiver.getEmail(), "/queue/chats", messageResponse);
        //send to receiver message status
        if (presenceService.isUserOnline(receiver.getEmail())){
            messageService.markMessageAsDelivered(messageResponse.id());
            simpMessagingTemplate.convertAndSendToUser(email, "/queue/status", new MessageStatusUpdate(messageResponse.id(),"DELIVERED"));
        }
        //send to sender message
        simpMessagingTemplate.convertAndSendToUser(email, "/queue/chats", messageResponse);


    }




}
