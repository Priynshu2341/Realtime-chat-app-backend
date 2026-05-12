package com.example.real_time_messaging_system.service;

import com.example.real_time_messaging_system.dto.request.RegisterRequest;
import com.example.real_time_messaging_system.entity.User;
import com.example.real_time_messaging_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String createUser(RegisterRequest request) {
       var savedUser = User
               .builder()
               .email(request.email())
               .password(passwordEncoder.encode(request.password()))
               .firstName(request.firstname())
               .lastName(request.lastname())
               .createdAt(LocalDateTime.now())
               .build();

       userRepository.save(savedUser);
       return "User created";
    }
}
