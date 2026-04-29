package com.example.real_time_messaging_system.websocket;

import com.example.real_time_messaging_system.dto.PresenceUpdate;
import com.example.real_time_messaging_system.entity.OnlineStatus;
import com.example.real_time_messaging_system.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
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


    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    private final Map<String, Integer> connections = new ConcurrentHashMap<>();

    public void userConnected(String email) {
        connections.merge(email,1, Integer::sum);
    }

    public void userDisconnected(String email) {
        connections.computeIfPresent(email, (k,v) -> v > 1 ? v - 1 : null );
    }


    public boolean isUserOnline(String email) {
        return connections.containsKey(email);
    }

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = accessor.getUser();
        if (user == null) return;
        String email = user.getName();
        boolean wasOffline =  !isUserOnline(email);
        userConnected(email);
        if (wasOffline) {
            simpMessagingTemplate.convertAndSend("/topic/presence",new PresenceUpdate(email, OnlineStatus.ONLINE));
        }
        messageService.handleUserCameOnline(email);


    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = accessor.getUser();

        if (user == null) return;

        String email = user.getName();

        log.info("user disconnected: {}", email);
        userDisconnected(email);
        if (!isUserOnline(email)){
            simpMessagingTemplate.convertAndSend("/topic/presence",new PresenceUpdate(email, OnlineStatus.OFFLINE));
        }
    }
}
