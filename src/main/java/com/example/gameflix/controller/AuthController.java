package com.example.gameflix.controller;

import com.example.gameflix.dto.AuthRequest;
import com.example.gameflix.dto.AuthResponse;
import com.example.gameflix.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody AuthRequest request) {
        String message = userService.registerUser(request);
        return new AuthResponse(message);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return userService.loginUser(request);
    }
}
