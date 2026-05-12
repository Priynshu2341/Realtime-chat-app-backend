package com.example.real_time_messaging_system.service;

import com.example.real_time_messaging_system.dto.response.ChatResponse;
import com.example.real_time_messaging_system.entity.*;
import com.example.real_time_messaging_system.repository.ChatRepository;
import com.example.real_time_messaging_system.repository.ChatUserRepository;
import com.example.real_time_messaging_system.repository.MessageRepository;
import com.example.real_time_messaging_system.repository.UserRepository;
import com.example.real_time_messaging_system.websocket.PresenceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {


    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChatMapper chatMapper;
    private final PresenceService presenceService;
    private final ChatUserRepository chatUserRepository;

    private String generateChatKey(Long id1, Long id2){
        return id1 < id2 ? id1 + "-" + id2 : id2 + "-" + id1;

    }

    public Chat findOrCreateChat(User sender,User receiver){
        String chatKey = generateChatKey(sender.getUserId(), receiver.getUserId());
        return chatRepository.findByChatKey(chatKey).orElseGet( ()-> {
            Chat chat = Chat
                    .builder()
                    .chatKey(chatKey)
                    .createdAt(LocalDateTime.now())
                    .build();
            Chat savedChat = chatRepository.save(chat);

             var c1 =  new ChatUser(null,sender,savedChat,null);
             var c2 =  new ChatUser(null,receiver,savedChat,null);

            chatUserRepository.saveAll(List.of(
                    c1,c2
            ));
            savedChat.setUsers(List.of(c1,c2));
            return savedChat;
        }
        );
    }


    public List<ChatResponse> findAllChats(Authentication authentication){
        var name = authentication.getName();
        var currentUser = userRepository.findByEmail(name).orElseThrow(()-> new EntityNotFoundException("User not found"));
        List<ChatUser> chatUsers = chatUserRepository.findByUserOrderByLastMessage(currentUser);


        return chatUsers.stream().map(chatUser -> {
            Chat chat = chatUser.getChat();
            User otherUser = chat.getUsers().stream().map(ChatUser::getUser).filter(user -> !user.getUserId().equals(currentUser.getUserId())).findFirst().orElseThrow(()-> new EntityNotFoundException("User not found"));
            long unReadCount = messageRepository.countUnreadMessages(chatUser.getLastReadAt(),chat.getId(), currentUser.getUserId());
            boolean isUserOnline = presenceService.isUserOnline(otherUser.getEmail());
            var lastMessage = chat.getLastMessage() != null ? chat.getLastMessage() : "";

            return chatMapper.toChatResponse(chat,currentUser,lastMessage,unReadCount,isUserOnline);
        }
        ).toList();


    }

    public void markAsRead(String email,Long chatId){
        var user = userRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("User not found"));
        var chat = chatRepository.findById(chatId).orElseThrow(()-> new EntityNotFoundException("Chat not found"));
        ChatUser chatUser = chatUserRepository.findByChatAndUser(chat,user).orElseThrow(()-> new RuntimeException("ChatUser Not Found"));
        chatUser.setLastReadAt(LocalDateTime.now());
        chatUserRepository.save(chatUser);

    }





}
