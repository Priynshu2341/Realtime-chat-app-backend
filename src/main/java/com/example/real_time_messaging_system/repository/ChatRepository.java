package com.example.real_time_messaging_system.repository;

import com.example.real_time_messaging_system.entity.Chat;
import com.example.real_time_messaging_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat,Long> {



        Optional<Chat> findByChatKey(String chatKey);
        List<Chat> findByUsers(User user);
}
