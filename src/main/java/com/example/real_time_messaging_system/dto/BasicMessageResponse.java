package com.example.real_time_messaging_system.dto;

import com.example.real_time_messaging_system.entity.User;

import java.time.LocalDateTime;

public record BasicMessageResponse(
        Long id,
        String content,
        Long senderId,
        LocalDateTime createdAt

) {
}
