package com.example.real_time_messaging_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages",indexes = {
        @Index(name = "idx_chat_id",columnList = "chat_id")
})
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @JoinColumn(nullable = false)
    private String content;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    private LocalDateTime createdAt;
}
