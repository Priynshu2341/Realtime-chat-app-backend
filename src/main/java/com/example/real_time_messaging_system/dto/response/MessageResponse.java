package com.example.real_time_messaging_system.dto.response;

import com.example.real_time_messaging_system.entity.MessageStatus;
import com.example.real_time_messaging_system.entity.MessageType;

import java.time.LocalDateTime;

public record MessageResponse(
        Long id,
        String content,
        Long senderId,
        String email,
        Long chatId,
        LocalDateTime createdAt,
        MessageStatus messageStatus,
        MessageType messageType,
        String mediaUrl
) {
}
