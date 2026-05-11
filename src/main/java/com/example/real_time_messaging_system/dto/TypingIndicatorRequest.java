package com.example.real_time_messaging_system.dto;

public record TypingIndicatorRequest(
        Long chatId,
        boolean typing
) {
}


