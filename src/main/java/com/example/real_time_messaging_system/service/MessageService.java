package com.example.real_time_messaging_system.service;

import com.example.real_time_messaging_system.dto.BasicMessageResponse;
import com.example.real_time_messaging_system.dto.MessageRequest;
import com.example.real_time_messaging_system.dto.MessageResponse;
import com.example.real_time_messaging_system.dto.PaginatedResponse;
import com.example.real_time_messaging_system.entity.Chat;
import com.example.real_time_messaging_system.entity.Message;
import com.example.real_time_messaging_system.repository.ChatRepository;
import com.example.real_time_messaging_system.repository.MessageRepository;
import com.example.real_time_messaging_system.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {


    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatService chatService;
    private final ChatRepository chatRepository;
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

         return new MessageResponse(
                 message.getId(),
                 message.getContent(),
                 sender.getUserId(),
                 sender.getEmail(),
                 chat.getId(),
                 message.getCreatedAt()
         );



    }




    public PaginatedResponse<BasicMessageResponse> findAllMessagesInChat(String email,String chatKey,LocalDateTime cursor) throws AccessDeniedException {
         var chat = chatRepository.findByChatKey(chatKey).orElseThrow(()-> new EntityNotFoundException("Invalid Chat Key"));
         var user = userRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("Invalid User Id"));
         if (!chat.getUsers().contains(user)){
             throw new AccessDeniedException("You are not allowed to view this chat");
         }

         List<Message> messages;

         if (cursor == null){
             messages = messageRepository.findTop10ByChatIdOrderByCreatedAtDesc(chat.getId());
         }else {
             messages = messageRepository.findTop10ByChatIdAndCreatedAtLessThanEqualOrderByCreatedAtDescIdDesc(chat.getId(),cursor);
         }
         var content = new java.util.ArrayList<>(messages.stream().map(messageMapper::toMessageResponse).toList());
         Collections.reverse(content);
         boolean hasMore = messages.size() == 10;
         LocalDateTime nextCursor = messages.isEmpty()?null:messages.get(messages.size()-1).getCreatedAt();

         return new PaginatedResponse<BasicMessageResponse>(content,hasMore,nextCursor);


    }



    public MessageResponse saveMessageFromSocket(String email, MessageRequest messageRequest){
         var sender = userRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("Invalid Sender Id"));
         var receiver = userRepository.findById(messageRequest.receiverId()).orElseThrow(()-> new EntityNotFoundException("Invalid Receiver Id"));
         var chat = chatService.findOrCreateChat(sender,receiver);

         var message = Message.builder()
                 .content(messageRequest.content())
                 .sender(sender)
                 .chat(chat)
                 .createdAt(LocalDateTime.now())
                 .build();

         messageRepository.save(message);

         return new MessageResponse(
                 message.getId(),
                 message.getContent(),
                 sender.getUserId(),
                 sender.getEmail(),
                 chat.getId(),
                 message.getCreatedAt()
         );
    }



}
