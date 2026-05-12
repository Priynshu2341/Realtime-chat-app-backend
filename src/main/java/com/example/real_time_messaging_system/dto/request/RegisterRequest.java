package com.example.real_time_messaging_system.dto.request;

public record RegisterRequest(
        String email,
        String password,
        String firstname,
        String lastname
) {
}
