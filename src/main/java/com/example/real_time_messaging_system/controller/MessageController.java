package com.example.real_time_messaging_system.controller;

import com.example.real_time_messaging_system.dto.BasicMessageResponse;
import com.example.real_time_messaging_system.dto.MessageCursor;
import com.example.real_time_messaging_system.dto.MessageRequest;
import com.example.real_time_messaging_system.dto.MessageResponse;
import com.example.real_time_messaging_system.entity.Chat;
import com.example.real_time_messaging_system.entity.Message;
import com.example.real_time_messaging_system.entity.User;
import com.example.real_time_messaging_system.repository.UserRepository;
import com.example.real_time_messaging_system.service.MessageService;
import jakarta.persistence.EntityNotFoundException;
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
    private final UserRepository userRepository;

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
        MessageResponse messageResponse = messageService.saveMessageFromSocket(email,messageRequest);
        simpMessagingTemplate.convertAndSend("/topic/chat/" + messageResponse.chatId(),messageResponse);
        simpMessagingTemplate.convertAndSendToUser(receiver.getEmail(), "/queue/chats", messageResponse);
        simpMessagingTemplate.convertAndSendToUser(email, "/queue/chats", messageResponse);
    }


}
