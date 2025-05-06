package com.kenny.taskboard.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String password;
    private String email;
    private String username;
    private String role;
}
