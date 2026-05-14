package com.example.real_time_messaging_system.redis;

import com.example.real_time_messaging_system.dto.PresenceEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PresenceSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void handle(String message) {
        try {
            PresenceEvent event = objectMapper.readValue(message,PresenceEvent.class);
            simpMessagingTemplate.convertAndSend("/topic/presence", event);
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }
}
