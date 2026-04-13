package com.example.real_time_messaging_system.repository;

import com.example.real_time_messaging_system.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {


}
