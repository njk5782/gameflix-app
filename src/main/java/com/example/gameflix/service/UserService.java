package com.example.gameflix.service;

import com.example.gameflix.dto.AuthRequest;
import com.example.gameflix.dto.AuthResponse;
import com.example.gameflix.model.User;
import com.example.gameflix.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String registerUser(AuthRequest request) {
        validateRequest(request);
        if (userRepository.existsByUsername(request.getUsername())) {
            return "Username already exists";
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(hashedPassword);

        userRepository.save(newUser);

        return "User registered successfully";
    }

    public AuthResponse loginUser(AuthRequest request) {
        validateRequest(request);
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());

        if (existingUser.isEmpty()) {
            return new AuthResponse("Invalid username or password");
        }

        User user = existingUser.get();

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String token = jwtService.createToken(user.getUsername());
            return new AuthResponse("Login successful", token);
        }

        return new AuthResponse("Invalid username or password");
    }

    private void validateRequest(AuthRequest request) {
        if (request == null || request.getUsername() == null || request.getUsername().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Username and password are required");
        }
    }
}
