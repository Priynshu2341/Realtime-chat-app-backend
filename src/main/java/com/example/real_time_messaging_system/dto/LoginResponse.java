package com.example.real_time_messaging_system.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        Long userId
) {
}
