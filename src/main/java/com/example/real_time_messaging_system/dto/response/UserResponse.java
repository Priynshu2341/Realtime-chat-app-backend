package com.example.real_time_messaging_system.dto.response;

import java.time.LocalDateTime;

public record UserResponse(
        Long userId,
        String email,
        String firstname,
        String lastname,
        LocalDateTime accountRegisteredAt
) {
}
