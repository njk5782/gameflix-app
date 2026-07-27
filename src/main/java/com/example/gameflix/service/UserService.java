package com.example.gameflix.service;

import com.example.gameflix.dto.AuthRequest;
import com.example.gameflix.model.User;
import com.example.gameflix.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String registerUser(AuthRequest request) {
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

    public String loginUser(AuthRequest request) {
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());

        if (existingUser.isEmpty()) {
            return "Invalid username or password";
        }

        User user = existingUser.get();

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return "Login successful";
        }

        return "Invalid username or password";
    }
}
