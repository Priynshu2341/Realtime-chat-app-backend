package com.example.real_time_messaging_system.service;

import com.example.real_time_messaging_system.dto.ChatResponse;
import com.example.real_time_messaging_system.entity.Chat;
import com.example.real_time_messaging_system.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMapper {

    public ChatResponse toChatResponse(Chat chat,User currentUser,String lastMessage){
        User otherUser = chat.getUsers().stream().filter(
                u -> !u.getUserId().equals(currentUser.getUserId())).findFirst().orElseThrow();

        return new ChatResponse(
                chat.getId(),
                otherUser.getUserId(),
                otherUser.getEmail(),
                lastMessage,
                chat.getChatKey()


        );
    }


}
