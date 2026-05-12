package com.example.real_time_messaging_system.dto.response;

import com.example.real_time_messaging_system.dto.MessageCursor;

import java.util.List;

public record PaginatedResponse<T>(
        List<T> content,
        Boolean hasMore,
        MessageCursor nextCursor,
        int elementsInPage
) {

}
