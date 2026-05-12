package com.example.real_time_messaging_system.dto.request;

import com.example.real_time_messaging_system.entity.MessageType;
import jakarta.validation.constraints.NotNull;

public record MessageRequest(
        @NotNull
        Long receiverId,
        @NotNull
        String content,
        String mediaUrl,
        MessageType messageType

) {
}
