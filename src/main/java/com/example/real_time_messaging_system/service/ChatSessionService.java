package com.example.real_time_messaging_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ChatSessionService {

    private final Map<Long,Long> activeChats = new ConcurrentHashMap<>();

    public void setActiveChats(Long userId, Long chatId) {
        activeChats.put(userId, chatId);
    }

    public void clearActiveChats(Long userId) {
        activeChats.remove(userId);
    }

    public Long getActiveChats(Long userId) {
        return activeChats.get(userId);
    }
}
