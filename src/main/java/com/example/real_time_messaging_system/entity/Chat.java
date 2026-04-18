package com.example.real_time_messaging_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Table
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "chat")
    private List<ChatUser> users;

    @OneToMany(mappedBy = "chat",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Message> messages;

    @Column(unique = true,nullable = false)
    private String chatKey;

    private LocalDateTime lastMessageAt;

    private String lastMessage;

    private LocalDateTime createdAt;
}
