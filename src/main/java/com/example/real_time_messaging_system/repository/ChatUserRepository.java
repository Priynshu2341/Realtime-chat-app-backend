package com.example.real_time_messaging_system.repository;

import com.example.real_time_messaging_system.entity.Chat;
import com.example.real_time_messaging_system.entity.ChatUser;
import com.example.real_time_messaging_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatUserRepository extends JpaRepository<ChatUser,Long> {

    Optional<ChatUser> findByChatAndUser(Chat chat, User user);
    List<ChatUser> findByUser(User user);

    @Query("""
            SELECT cu FROM ChatUser cu
            WHERE cu.user = :user
            ORDER BY cu.chat.lastMessageAt DESC
""")
    List<ChatUser> findByUserOrderByLastMessage(@Param("user") User user);


}
