package com.kenny.taskboard.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
// import com.kenny.taskboard.model.User;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails; 
// import org.springframework.stereotype.Component;
import org.springframework.stereotype.Component;

import java.util.Date;
// import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration.ms}")
    private int expiration;

    public String generateToken(Authentication authentication){
        // I'll use this method to generate the token
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        String username = userPrincipal.getUsername();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return JWT.create()
            .withSubject(username)
            .withIssuedAt(now)
            .withExpiresAt(expiryDate)
            .sign(Algorithm.HMAC512(jwtSecret));
    }

    public String getUsernameFromJWT(String token) {
        return JWT.require(Algorithm.HMAC512(jwtSecret))
                .build()
                .verify(token)
                .getSubject();
    }
    public boolean validateToken(String authToken) {
        try {
            JWT.require(Algorithm.HMAC512(jwtSecret)).build().verify(authToken);
            return true;
        } catch (Exception ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
        }
        return false;
    }
}
