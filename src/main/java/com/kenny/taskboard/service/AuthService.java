package com.kenny.taskboard.service;

import com.kenny.taskboard.dto.LoginRequest;
import com.kenny.taskboard.dto.RegisterRequest;
import com.kenny.taskboard.model.User;

public interface AuthService {
    User login(LoginRequest loginRequest);
    User registerUser(RegisterRequest registerRequest);
    String generateToken(User user);
    String getUsernameFromToken(String token);
}
