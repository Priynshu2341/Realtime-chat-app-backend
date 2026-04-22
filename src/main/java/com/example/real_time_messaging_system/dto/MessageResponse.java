package com.example.real_time_messaging_system.dto;

import com.example.real_time_messaging_system.entity.MessageStatus;

import java.time.LocalDateTime;

public record MessageResponse(
        Long id,
        String content,
        Long senderId,
        String email,
        Long chatId,
        LocalDateTime createdAt,
        MessageStatus messageStatus

) {
}
