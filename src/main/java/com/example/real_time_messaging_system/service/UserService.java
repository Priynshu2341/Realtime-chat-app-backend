package com.example.real_time_messaging_system.service;

import com.example.real_time_messaging_system.dto.request.RegisterRequest;
import com.example.real_time_messaging_system.dto.response.UserResponse;
import com.example.real_time_messaging_system.entity.User;
import com.example.real_time_messaging_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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


    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return null;
        }
        return new UserResponse(
                user.getUserId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreatedAt()
        );

    };

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user ->
                new UserResponse(
                user.getUserId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreatedAt()
        )).toList();

    }

}
