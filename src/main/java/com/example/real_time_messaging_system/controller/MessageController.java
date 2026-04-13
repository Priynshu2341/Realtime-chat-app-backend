package com.example.real_time_messaging_system.controller;

import com.example.real_time_messaging_system.dto.MessageRequest;
import com.example.real_time_messaging_system.entity.Message;
import com.example.real_time_messaging_system.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/message")
@RequiredArgsConstructor
public class MessageController {


    private final MessageService messageService;

    @PostMapping("/send")
    public String sendMessage(Authentication authentication,@RequestBody MessageRequest messageRequest){
        return messageService.saveMessages(messageRequest,authentication);

    }


}
