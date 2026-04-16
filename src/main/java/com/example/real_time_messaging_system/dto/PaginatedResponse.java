package com.example.real_time_messaging_system.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PaginatedResponse<T>(
        List<T> content,
        Boolean hasMore,
        MessageCursor nextCursor
) {

}
