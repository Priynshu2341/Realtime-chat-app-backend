package com.example.real_time_messaging_system.repository;

import com.example.real_time_messaging_system.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {


   Optional<Message> findTopByChatIdOrderByCreatedAtDesc(Long chatId);
   List<Message> findTop20ByChatIdOrderByCreatedAtDescIdDesc(Long chatId);

   @Query("""
         SELECT m from Message m
         WHERE m.chat.id = :chatId
         AND (
             m.createdAt < :createdAt
             OR (m.createdAt = :createdAt AND m.id < :id)
         )
         ORDER BY m.createdAt DESC, m.id DESC
""")
   List<Message> findNextMessage(
           @Param("chatId") Long chatId,
           @Param("createdAt") LocalDateTime createdAt,
           @Param("id") Long id,
           Pageable pageable);

   @Query("""
          SELECT COUNT(m)
          FROM Message m
          WHERE m.chat.id = :chatId
          AND m.createdAt > :lastReadAt
          AND m.sender.userId != :userId
        """)
   long countUnreadMessages(
           @Param("lastReadAt") LocalDateTime lastReadAt,
           @Param("chatId") Long chatId,
           @Param("userId") Long userId);
}
