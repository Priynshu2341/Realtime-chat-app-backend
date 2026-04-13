package com.example.real_time_messaging_system.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UniqueElements;

public record LoginRequest(
        @NotBlank
        String email,
        @NotBlank
        String password

) {
}
