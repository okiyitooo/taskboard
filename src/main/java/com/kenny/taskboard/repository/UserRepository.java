package com.kenny.taskboard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kenny.taskboard.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Define methods for CRUD operations and custom queries here
    // For example:
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    // List<User> findAll();
    // void save(User user);
    // void deleteById(Long id);
}
