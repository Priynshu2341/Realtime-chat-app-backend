package com.example.real_time_messaging_system.dto.request;

public record TypingIndicatorRequest(
        Long chatId,
        boolean typing
) {
}


