package com.kenny.taskboard.service.impl;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kenny.taskboard.dto.BoardRequest;
import com.kenny.taskboard.dto.BoardResponse;
import com.kenny.taskboard.model.Board;
import com.kenny.taskboard.model.User;
import com.kenny.taskboard.repository.BoardRepository;
import com.kenny.taskboard.repository.UserRepository;
import com.kenny.taskboard.service.BoardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    final private UserRepository userRepository;
    private final BoardRepository boardRepository;
    
    @Override
    public BoardResponse createBoard(BoardRequest boardRequest, String username) {
        /* 
         * createBoard(BoardRequest boardRequest, String username):
            Find user by username.
            Create Board entity, set owner, save.
            Return BoardResponse or Board.
         */
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        Board board = Board.builder()
                .title(boardRequest.getTitle())
                .description(boardRequest.getDescription())
                .owner(user)
                .build();
        boardRepository.save(board);
        return BoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .description(board.getDescription())
                .ownerName(board.getOwner().getUsername())
                .build();
        
    }

    @Override
    public List<BoardResponse> getAllBoardsByOwner(String username) {
        /*
         * Find user by username, then use boardRepository.findByOwnerId().
            Map to List<BoardResponse>.
        */
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        List<Board> boards = boardRepository.findAllByOwnerId(user.getId());
        return boards.stream()
                .map(board -> BoardResponse.builder()
                        .id(board.getId())
                        .title(board.getTitle())
                        .description(board.getDescription())
                        .ownerName(board.getOwner().getUsername())
                        .build())
                .toList();
    }

    @Override
    public BoardResponse getBoardById(Long id, String username) {
        /*
         * Fetch board.
            Crucial: Check if the fetched board's owner matches the logged-in username. Throw exception if not.
            Map to BoardResponse.
         */
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Board not found with id: " + id));
        if (!board.getOwner().getUsername().equals(username)) {
            throw new UsernameNotFoundException("Board not found with id: " + id + " for user: " + username);
        }
        return BoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .description(board.getDescription())
                .ownerName(board.getOwner().getUsername())
                .build();
    }

    @Override
    public BoardResponse updateBoard(Long id, BoardRequest boardRequest, String username) {
        /*
            Fetch board.
            Crucial: Check ownership.
            Update fields, save.
            Map to BoardResponse.
        */
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Board not found with id: " + id));
        if (!board.getOwner().getUsername().equals(username)) {
            throw new UsernameNotFoundException("Board not found with id: " + id + " for user: " + username);
        }
        board.setTitle(boardRequest.getTitle()==null?board.getTitle():boardRequest.getTitle());
        board.setDescription(boardRequest.getDescription()!=null?boardRequest.getDescription():board.getDescription());
        boardRepository.save(board);
        return BoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .description(board.getDescription())
                .ownerName(board.getOwner().getUsername())
                .build();
    }

    @Override
    public void deleteBoard(Long id, String username) {
        /* Fetch board.
            Crucial: Check ownership.
            Delete. */
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Board not found with id: " + id));
        if (!board.getOwner().getUsername().equals(username)) {
            throw new UsernameNotFoundException("Board not found with id: " + id + " for user: " + username);
        }
        boardRepository.delete(board);
    }

    @Override
    public List<BoardResponse> getAllBoards() {
        /* 
         * Find user by username, then use boardRepository.findByOwnerId().
            Map to List<BoardResponse>.
        */
        List<Board> boards = boardRepository.findAll();
        return boards.stream()
                .map(board -> BoardResponse.builder()
                        .id(board.getId())
                        .title(board.getTitle())
                        .description(board.getDescription())
                        .ownerName(board.getOwner().getUsername())
                        .build())
                .toList();
    }
    
}
