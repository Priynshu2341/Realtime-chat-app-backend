package com.example.real_time_messaging_system.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MessageRequest(
        @NotNull
        Long receiverId,
        @NotNull
        String content

) {
}
