package com.example.real_time_messaging_system.websocket;

import com.example.real_time_messaging_system.dto.PresenceEvent;
import com.example.real_time_messaging_system.dto.UserOnlineEvent;
import com.example.real_time_messaging_system.entity.OnlineStatus;
import com.example.real_time_messaging_system.redis.PresencePublisher;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
@RequiredArgsConstructor
@Slf4j
public class PresenceService {



    private final ApplicationEventPublisher eventPublisher;
    private final PresencePublisher presencePublisher;
    private final StringRedisTemplate stringRedisTemplate;


    public String getPresenceKey(String email){
        return "presence:" + email;
    }

    public boolean isUserOnline(String email) {
        String value = stringRedisTemplate
                .opsForValue()
                .get(getPresenceKey(email));

        if (value == null) {
            return false;
        }
        try {
            return Integer.parseInt(value) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }



    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = accessor.getUser();
        if (user == null) return;
        String email = user.getName();
        long connections = stringRedisTemplate.opsForValue().increment(getPresenceKey(email));
        boolean wasOffline =  connections == 1;

        if (wasOffline) {
            presencePublisher.publish(new PresenceEvent(email,OnlineStatus.ONLINE));
            eventPublisher.publishEvent(new UserOnlineEvent(email));
        }


    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = accessor.getUser();
        if (user == null) return;
        String email = user.getName();
        long connections = stringRedisTemplate.opsForValue().decrement(getPresenceKey(email));
        boolean isOffline = connections <= 0;

        if (isOffline) {
            stringRedisTemplate.delete(getPresenceKey(email));
            presencePublisher.publish(new PresenceEvent(email,OnlineStatus.OFFLINE));
        }


    }
}
