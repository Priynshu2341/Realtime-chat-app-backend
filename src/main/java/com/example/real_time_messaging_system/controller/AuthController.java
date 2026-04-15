package com.example.real_time_messaging_system.controller;


import com.example.real_time_messaging_system.dto.LoginRequest;
import com.example.real_time_messaging_system.dto.LoginResponse;
import com.example.real_time_messaging_system.dto.RegisterRequest;
import com.example.real_time_messaging_system.repository.UserRepository;
import com.example.real_time_messaging_system.security.CustomUserDetailsService;
import com.example.real_time_messaging_system.security.JwtUtility;
import com.example.real_time_messaging_system.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtility jwtUtility;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(userService.createUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
         authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
          var user = userDetailsService.loadUserByUsername(loginRequest.email());
          var userId = userRepository.findByEmail(loginRequest.email()).get().getUserId();

            var accessToken = jwtUtility.createAccessToken(user);
            var refreshToken = jwtUtility.createRefreshToken(user);
            return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken,userId));

    }
}
