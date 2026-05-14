package com.example.real_time_messaging_system.controller;

import com.example.real_time_messaging_system.dto.response.UserResponse;
import com.example.real_time_messaging_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {


    private final UserService userService;

    @GetMapping("/find")
    public UserResponse findUserByEmail(@RequestParam("email") String email){
       return userService.getUserByEmail(email);
    }

    @GetMapping("/find/all")
    public List<UserResponse> findAllUser(){
        return userService.getAllUsers();
    }
}
