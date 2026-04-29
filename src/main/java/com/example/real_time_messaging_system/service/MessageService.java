package com.example.real_time_messaging_system.service;

import com.example.real_time_messaging_system.dto.*;
import com.example.real_time_messaging_system.entity.Chat;
import com.example.real_time_messaging_system.entity.ChatUser;
import com.example.real_time_messaging_system.entity.Message;
import com.example.real_time_messaging_system.entity.MessageStatus;
import com.example.real_time_messaging_system.repository.ChatRepository;
import com.example.real_time_messaging_system.repository.ChatUserRepository;
import com.example.real_time_messaging_system.repository.MessageRepository;
import com.example.real_time_messaging_system.repository.UserRepository;
import com.example.real_time_messaging_system.websocket.PresenceService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {


    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatService chatService;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;
    private final ChatSessionService chatSessionService;
    private final SimpMessagingTemplate simpMessagingTemplate;

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
                .messageStatus(MessageStatus.SENT)
                .createdAt(LocalDateTime.now())
                .build();

        messageRepository.save(message);
        chat.setLastMessage(message.getContent());
        chat.setLastMessageAt(message.getCreatedAt());
        chatRepository.save(chat);

         return new MessageResponse(
                 message.getId(),
                 message.getContent(),
                 sender.getUserId(),
                 sender.getEmail(),
                 chat.getId(),
                 message.getCreatedAt(),
                 message.getMessageStatus()
         );



    }




    public PaginatedResponse<BasicMessageResponse> findAllMessagesInChat(String email, String chatKey, MessageCursor cursor) throws AccessDeniedException {
         var chat = chatRepository.findByChatKey(chatKey).orElseThrow(()-> new EntityNotFoundException("Invalid Chat Key"));
         var user = userRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("Invalid User Id"));
         var chatUser = chat.getUsers();
         boolean isParticipant = chatUser.stream().anyMatch(u -> u.getUser().getUserId().equals(user.getUserId()));
         if (!isParticipant) {
             throw new AccessDeniedException("Access Denied");
         }
         List<Message> messages;

         if (cursor == null){
             messages = messageRepository.findTop20ByChatIdOrderByCreatedAtDescIdDesc(chat.getId());
         }else {
             messages = messageRepository.findNextMessage(
                     chat.getId(),
                     cursor.createdAt(),
                     cursor.id(),
                     PageRequest.of(0,20));
         }
         var content = new java.util.ArrayList<>(messages.stream().map(messageMapper::toMessageResponse).toList());
         Collections.reverse(content);
         boolean hasMore = messages.size() == 20;
         MessageCursor nextCursor = messages.isEmpty() ? null : new MessageCursor(
                 messages.get(messages.size() - 1).getCreatedAt(),
                 messages.get(messages.size() -1).getId()

         );

         return new PaginatedResponse<BasicMessageResponse>(content,hasMore,nextCursor,messages.size());


    }



    public MessageResponse saveMessageFromSocket(String email, MessageRequest messageRequest,boolean isOnline){
         var sender = userRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("Invalid Sender Id"));
         var receiver = userRepository.findById(messageRequest.receiverId()).orElseThrow(()-> new EntityNotFoundException("Invalid Receiver Id"));
         var chat = chatService.findOrCreateChat(sender,receiver);

         Long activeChat = chatSessionService.getActiveChats(receiver.getUserId());

         MessageStatus messageStatus;
         boolean isChatSame = activeChat != null && activeChat.equals(chat.getId());

         if (isOnline && isChatSame) {
             messageStatus = MessageStatus.READ;
         }else if (isOnline) {
             messageStatus = MessageStatus.DELIVERED;
         }else {
             messageStatus = MessageStatus.SENT;
         }

         var message = Message.builder()
                 .content(messageRequest.content())
                 .sender(sender)
                 .chat(chat)
                 .messageStatus(messageStatus)
                 .createdAt(LocalDateTime.now())
                 .build();

         messageRepository.save(message);
         chat.setLastMessage(message.getContent());
         chat.setLastMessageAt(message.getCreatedAt());
         chatRepository.save(chat);

         if (isOnline && isChatSame) {
             messageRepository.markAllDeliveredToSeen(receiver.getUserId(),chat.getId());
         }


         return new MessageResponse(
                 message.getId(),
                 message.getContent(),
                 sender.getUserId(),
                 sender.getEmail(),
                 chat.getId(),
                 message.getCreatedAt(),
                 message.getMessageStatus()

         );
    }

    public void markMessageAsDelivered(Long messageId){
         var message = messageRepository.findById(messageId).orElseThrow(()-> new EntityNotFoundException("Message not found"));
         if (message.getMessageStatus() == MessageStatus.SENT){
             message.setMessageStatus(MessageStatus.DELIVERED);
             messageRepository.save(message);
         }

    }


    @Transactional
    public void handleUserCameOnline(String email){
        var receiver =  userRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("Invalid User Id"));
        Set<Long> userIds = messageRepository.findAllUserIdForSentMessages(receiver.getUserId());
        if (userIds.isEmpty()) return;
        var messagesIds = messageRepository.findAllSentMessageIdForUser(receiver.getUserId());
        messageRepository.markAllSentToDelivered(receiver.getUserId());
        for (Long userId : userIds) {
            var currentUser = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
            simpMessagingTemplate.convertAndSendToUser(currentUser.getEmail(), "/queue/refresh", messagesIds);
        }

    }


    @Transactional
    public void markAsSeen(Long chatId,Long userId){
         Set<Long> messagesIds = messageRepository.findAllMessageIdDeliveredInChat(userId,chatId);
         Set<Long> userIds = messageRepository.getSenderIdInMsg(userId, chatId);
         messageRepository.markAllDeliveredToSeen(userId, chatId);

         for (Long ids : userIds) {
             var sender = userRepository.findById(ids).orElseThrow(()-> new EntityNotFoundException("Sender not found"));
             simpMessagingTemplate.convertAndSendToUser(sender.getEmail(), "/queue/seen", messagesIds);

         }

    }



}
