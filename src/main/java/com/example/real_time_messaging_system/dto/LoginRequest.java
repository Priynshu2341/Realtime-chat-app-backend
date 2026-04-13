package com.example.real_time_messaging_system.dto;

import jakarta.persistence.Column;

public record LoginRequest(
        @Column(unique = true,nullable = false)
        String email,
        @Column(nullable = false)
        String password

) {
}
