package com.kenny.taskboard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kenny.taskboard.model.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOwnerId(Long ownerId);
    Optional<Board> findByIdAndOwnerId(Long id, Long ownerId);
}
