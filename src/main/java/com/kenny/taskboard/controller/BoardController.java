package com.kenny.taskboard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kenny.taskboard.dto.BoardRequest;
import com.kenny.taskboard.dto.BoardResponse;
import com.kenny.taskboard.model.User;
import com.kenny.taskboard.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(@RequestBody BoardRequest boardRequest, @AuthenticationPrincipal User principal) {
        if (principal == null) {
            log.error("User is not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (boardRequest.getTitle() == null || boardRequest.getDescription() == null) {
            log.error("BoardRequest is invalid");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        log.info("Creating board with title: {}", boardRequest.getTitle());
        BoardResponse boardResponse = boardService.createBoard(boardRequest, principal.getUsername());
         return ResponseEntity.status(HttpStatus.CREATED).body(boardResponse);
    }
    @GetMapping
    public ResponseEntity<?> getAllBoards() {
        return ResponseEntity.ok(boardService.getAllBoards());
    }
    @GetMapping("/owner/{username}")
    public ResponseEntity<?> getAllBoardsByOwner(@AuthenticationPrincipal User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(boardService.getAllBoardsByOwner(principal.getUsername()));
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<BoardResponse> getByOd(@PathVariable Long id, @AuthenticationPrincipal User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(boardService.getBoardById(id, principal.getUsername()));
    }
    @PutMapping("/{id}")
    public ResponseEntity<BoardResponse> updateBoard(@PathVariable Long id, @RequestBody BoardRequest boardRequest, @AuthenticationPrincipal User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (boardRequest.getTitle() == null && boardRequest.getDescription() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(boardService.updateBoard(id, boardRequest, principal.getUsername()));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long id, @AuthenticationPrincipal User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        boardService.deleteBoard(id, principal.getUsername());
        return ResponseEntity.ok("Board deleted successfully");
    }
}