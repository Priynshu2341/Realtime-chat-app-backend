package com.example.real_time_messaging_system.service;

import com.example.real_time_messaging_system.dto.response.BasicMessageResponse;
import com.example.real_time_messaging_system.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageMapper {


    public BasicMessageResponse toMessageResponse(Message message){
        return new BasicMessageResponse(
                message.getId(),
                message.getContent(),
                message.getSender().getUserId(),
                message.getCreatedAt(),
                message.getMessageStatus(),
                message.getMessageType(),
                message.getMediaUrl()
        );
    }
}
