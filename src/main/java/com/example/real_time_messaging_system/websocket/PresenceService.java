package com.example.real_time_messaging_system.websocket;

import com.example.real_time_messaging_system.entity.Message;
import com.example.real_time_messaging_system.entity.MessageStatus;
import com.example.real_time_messaging_system.repository.MessageRepository;
import com.example.real_time_messaging_system.repository.UserRepository;
import com.example.real_time_messaging_system.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PresenceService {

    private final SimpUserRegistry simpUserRegistry;
    private final MessageService messageService;


    public boolean isUserOnline(String email) {
       return simpUserRegistry.getUsers().stream().anyMatch(u -> u.getName().equals(email));
    }

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = accessor.getUser();
        if (user == null) return;
        String email = user.getName();
        log.info("user connected: {}", email);
        messageService.handleUserCameOnline(email);

    }
}
