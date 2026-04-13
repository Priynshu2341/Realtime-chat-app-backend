package com.example.real_time_messaging_system.service;

import com.example.real_time_messaging_system.dto.ChatResponse;
import com.example.real_time_messaging_system.dto.MessageRequest;
import com.example.real_time_messaging_system.entity.Chat;
import com.example.real_time_messaging_system.entity.Message;
import com.example.real_time_messaging_system.entity.User;
import com.example.real_time_messaging_system.repository.ChatRepository;
import com.example.real_time_messaging_system.repository.MessageRepository;
import com.example.real_time_messaging_system.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {


    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChatMapper chatMapper;

    private String generateChatKey(Long id1, Long id2){
        return id1 < id2 ? id1 + "-" + id2 : id2 + "-" + id1;



    }

    public Chat findOrCreateChat(User sender,User receiver){
        String chatKey = generateChatKey(sender.getUserId(), receiver.getUserId());
        return chatRepository.findByChatKey(chatKey).orElseGet( ()-> {
            Chat chat = Chat
                    .builder()
                    .chatKey(chatKey)
                    .users(new ArrayList<>(List.of(sender,receiver)))
                    .createdAt(LocalDateTime.now())
                    .build();

            sender.getChats().add(chat);
            receiver.getChats().add(chat);
            return chatRepository.save(chat);
        }
        );
    }


    public List<ChatResponse> findAllChats(Authentication authentication){
        var name = authentication.getName();
        var currentUser = userRepository.findByEmail(name).orElseThrow(()-> new EntityNotFoundException("User not found"));
        var chats = chatRepository.findByUsers(currentUser);
        return chats.stream().map(chat -> {
            var lastMessage = messageRepository
                    .findTopByChatIdOrderByCreatedAtDesc(chat.getId())
                    .map(Message::getContent)
                    .orElse("");

            return chatMapper.toChatResponse(chat,currentUser,lastMessage);
        }


        ).toList();


    }



}
