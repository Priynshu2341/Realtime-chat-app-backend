package com.example.real_time_messaging_system.redis;

import com.example.real_time_messaging_system.dto.PresenceEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PresencePublisher {

    private static final String CHANNEL = "presence";
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate stringRedisTemplate;

    public void publish(PresenceEvent event){

        try {
            String payload = objectMapper.writeValueAsString(event);
            stringRedisTemplate.convertAndSend(CHANNEL,payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
