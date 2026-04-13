package com.example.real_time_messaging_system.dto;

import java.time.LocalDateTime;

public record MessageResponse(
        Long id,
        String content,
        String email,
        Long chatId,
        LocalDateTime createdAt
) {
}
