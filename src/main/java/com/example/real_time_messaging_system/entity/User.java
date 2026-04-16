package com.example.real_time_messaging_system.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "users")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String firstName;
    private String lastName;

    @Column(unique = true,nullable = false)
    private String email;
    @JsonIgnore
    private String password;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "chat_users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id")
    )
    private List<Chat> chats;

    @JsonIgnore
    @OneToMany(mappedBy = "sender",fetch = FetchType.LAZY)
    private List<Message> sentMessages;

    private LocalDateTime createdAt;


}
