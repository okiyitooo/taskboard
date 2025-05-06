package com.kenny.taskboard.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kenny.taskboard.dto.LoginRequest;
import com.kenny.taskboard.dto.RegisterRequest;
import com.kenny.taskboard.model.User;
import com.kenny.taskboard.repository.UserRepository;
import com.kenny.taskboard.service.AuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return user;
    }

    @Override
    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(registerRequest.getRole() != null ? registerRequest.getRole() : "USER"); // Default role
        return userRepository.save(user);
    }

    @Override
    public String generateToken(User user) {
        throw new UnsupportedOperationException("Token generation not implemented yet");
    }

    @Override
    public String getUsernameFromToken(String token) {
        throw new UnsupportedOperationException("Token parsing not implemented yet");
    }
    
}
