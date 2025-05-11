package com.kenny.taskboard.service;

import com.kenny.taskboard.dto.BoardRequest;
import com.kenny.taskboard.dto.BoardResponse;
import java.util.List;

public interface BoardService {
    BoardResponse createBoard(BoardRequest boardRequest, String username);
    List<BoardResponse> getAllBoardsByOwner(String username);
    BoardResponse getBoardById(Long id, String username);
    BoardResponse updateBoard(Long id, BoardRequest boardRequest, String username);
    void deleteBoard(Long id, String username);
    List<BoardResponse> getAllBoards();
}
