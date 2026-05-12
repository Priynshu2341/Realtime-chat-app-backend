package com.example.real_time_messaging_system.dto.response;

public record TypingIndicatorResponse(
        Long chatId,
        Long senderId,
        boolean typing
) {
}
