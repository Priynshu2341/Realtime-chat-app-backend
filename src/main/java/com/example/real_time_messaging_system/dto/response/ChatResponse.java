package com.example.real_time_messaging_system.dto.response;

public record ChatResponse(
        Long chatId,
        Long otherUserId,
        String otherUserName,
        String lastMessage,
        String chatKey,
        long unread,
        boolean isOtherUserOnline
) {
}
