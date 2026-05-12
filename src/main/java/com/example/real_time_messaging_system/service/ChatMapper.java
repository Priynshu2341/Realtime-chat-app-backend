package com.example.real_time_messaging_system.service;

import com.example.real_time_messaging_system.dto.response.ChatResponse;
import com.example.real_time_messaging_system.entity.Chat;
import com.example.real_time_messaging_system.entity.ChatUser;
import com.example.real_time_messaging_system.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMapper {

    public ChatResponse toChatResponse(Chat chat,User currentUser,String lastMessage,long unReadCount,boolean isUserOnline){
        ChatUser otherUser = chat.getUsers().stream().filter(
                u -> !u.getUser().getUserId().equals(currentUser.getUserId())).findFirst().orElseThrow();

        return new ChatResponse(
                chat.getId(),
                otherUser.getUser().getUserId(),
                otherUser.getUser().getEmail(),
                lastMessage,
                chat.getChatKey(),
                unReadCount,
                isUserOnline



        );
    }


}
