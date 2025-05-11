package com.kenny.taskboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kenny.taskboard.service.CustomUserDetailsService;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public JwtAuthentificationFilter jwtAuthenticationFilter() {
        // Define a JwtAuthenticationFilter bean to handle JWT authentication
        // This filter will intercept requests and validate the JWT token
        return new JwtAuthentificationFilter(customUserDetailsService, jwtTokenProvider());
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        // Define a JwtTokenProvider bean to generate and validate JWT tokens
        // This class will contain methods to create tokens, extract user information, and validate tokens
        return new JwtTokenProvider();
    }
    // Security configuration for the application
    // This class will contain methods to configure security settings, authentication, and authorization
    // For example, you can use Spring Security to secure your REST endpoints and configure user roles and permissions

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Define a PasswordEncoder bean to encode passwords
        // You can use BCryptPasswordEncoder or any other encoder of your choice
        return new BCryptPasswordEncoder();
    }

    @Bean
    public  AuthenticationManager authenticationManager(AuthenticationConfiguration authz) throws Exception {
        // Define an AuthenticationManager bean to manage authentication
        // This bean will be used to authenticate users based on their credentials
        return authz.getAuthenticationManager();
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.disable())
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/test/**").authenticated()
                .requestMatchers("/api/boards/**").authenticated()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .httpBasic(withDefaults());

        // Add JWT filter
        return http.build();
    }
    // AuthenticationManager bean
}
