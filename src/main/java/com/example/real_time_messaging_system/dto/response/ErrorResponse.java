package com.example.real_time_messaging_system.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ErrorResponse {
    private int status;

    private String content;

    private LocalDateTime localDateTime;


}
