package com.example.real_time_messaging_system.service;

import com.example.real_time_messaging_system.dto.MessageRequest;
import com.example.real_time_messaging_system.entity.Chat;
import com.example.real_time_messaging_system.entity.Message;
import com.example.real_time_messaging_system.repository.MessageRepository;
import com.example.real_time_messaging_system.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {


    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatService chatService;



     public String saveMessages(@Valid MessageRequest messageRequest, Authentication authentication){
        String email = authentication.getName();
        var sender = userRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("Invalid Sender Id"));
        var receiver = userRepository.findById(messageRequest.receiverId()).orElseThrow(()-> new EntityNotFoundException("Invalid Receiver Id"));

        Chat chat = chatService.findOrCreateChat(sender,receiver);

        var message = Message
                .builder()
                .content(messageRequest.content())
                .sender(sender)
                .chat(chat)
                .createdAt(LocalDateTime.now())
                .build();

        messageRepository.save(message);


        return "Message saved";


    }

}
