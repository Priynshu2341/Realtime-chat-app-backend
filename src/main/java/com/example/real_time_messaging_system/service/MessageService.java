package com.example.real_time_messaging_system.service;

import com.example.real_time_messaging_system.dto.BasicMessageResponse;
import com.example.real_time_messaging_system.dto.MessageRequest;
import com.example.real_time_messaging_system.dto.MessageResponse;
import com.example.real_time_messaging_system.entity.Chat;
import com.example.real_time_messaging_system.entity.Message;
import com.example.real_time_messaging_system.repository.ChatRepository;
import com.example.real_time_messaging_system.repository.MessageRepository;
import com.example.real_time_messaging_system.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {


    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatService chatService;
    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageMapper messageMapper;

     public MessageResponse saveMessages(@Valid MessageRequest messageRequest, Authentication authentication){
        String email = authentication.getName();
        var sender = userRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("Invalid Sender Id"));
        var receiver = userRepository.findById(messageRequest.receiverId()).orElseThrow(()-> new EntityNotFoundException("Invalid Receiver Id"));

        Chat chat = chatService.findOrCreateChat(sender,receiver);

        var message = Message
                .builder()
                .content(messageRequest.content())
                .sender(sender)
                .chat(chat)
                .createdAt(LocalDateTime.now())
                .build();

        messageRepository.save(message);

         MessageResponse messageResponse = new MessageResponse(
                 message.getId(),
                 message.getContent(),
                 sender.getEmail(),
                 chat.getId(),
                 message.getCreatedAt()
         );

         messagingTemplate.convertAndSend("/topic/chat/"+ chat.getId() ,messageResponse);
         System.out.println("sending message: /topic/chat/"+ chat.getId());


        return messageResponse;


    }
    public List<BasicMessageResponse> findAllMessagesInChat(String chatKey){
         var chat = chatRepository.findByChatKey(chatKey).orElseThrow(()-> new EntityNotFoundException("Invalid Chat Key"));
         return messageRepository.findByChatId(chat.getId())
                 .stream().map(messageMapper::toMessageResponse).toList();


    }

}
