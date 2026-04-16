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

    @ManyToMany(mappedBy = "chats")
    private List<User> users;

    @OneToMany(mappedBy = "chat",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Message> messages;

    @Column(unique = true,nullable = false)
    private String chatKey;

    private LocalDateTime createdAt;
}
