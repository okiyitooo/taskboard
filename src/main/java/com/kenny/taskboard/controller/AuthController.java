package com.kenny.taskboard.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kenny.taskboard.dto.AuthResponse;
import com.kenny.taskboard.dto.LoginRequest;
import com.kenny.taskboard.dto.RegisterRequest;
import com.kenny.taskboard.model.User;
import com.kenny.taskboard.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    final private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try{
            User user = authService.registerUser(registerRequest);
            return ResponseEntity.ok(user);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try{
            User user = authService.login(loginRequest);
            return ResponseEntity.ok(new AuthResponse("dummy_token" + user.getId() + user.getUsername()));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    
}
