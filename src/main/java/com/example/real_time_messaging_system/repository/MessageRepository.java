package com.example.real_time_messaging_system.repository;

import com.example.real_time_messaging_system.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {


   Optional<Message> findTopByChatIdOrderByCreatedAtDesc(Long chatId);
   List<Message> findTop10ByChatIdOrderByCreatedAtDesc(Long chatId);
   List<Message> findTop10ByChatIdAndCreatedAtLessThanEqualOrderByCreatedAtDescIdDesc(
           Long chatId,
           LocalDateTime createdAt
   );
}
