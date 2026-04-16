package com.example.real_time_messaging_system.dto;

import java.time.LocalDateTime;

public record MessageCursor(
        LocalDateTime createdAt,
        Long id
) {

}
