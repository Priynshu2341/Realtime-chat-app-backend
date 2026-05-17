package com.example.real_time_messaging_system.websocket;


import com.example.real_time_messaging_system.dto.UserOnlineEvent;
import com.example.real_time_messaging_system.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PresenceHandler {

    private final MessageService messageService;


    @EventListener
    public void handleUserCameOnline(UserOnlineEvent event){
        messageService.handleUserCameOnline(event.email());
    }
}
