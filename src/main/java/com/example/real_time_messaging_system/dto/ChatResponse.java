package com.example.real_time_messaging_system.dto;

import java.util.List;

public record ChatResponse(
        Long chatId,
        Long otherUserId,
        String otherUserName,
        String lastMessage,
        String chatKey,
        long unRead
) {
}
