package com.example.real_time_messaging_system.dto.response;

import com.example.real_time_messaging_system.entity.MessageStatus;

import java.time.LocalDateTime;

public record BasicMessageResponse(
        Long id,
        String content,
        Long senderId,
        LocalDateTime createdAt,
        MessageStatus messageStatus

) {
}
