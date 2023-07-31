package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.Board;
import ch.uzh.ifi.seal.soprafs19.service.BoardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class BoardController {
    private final BoardService boardService;

    @Autowired
    private BoardController(BoardService boardService) {
        this.boardService = boardService;
    }


    @GetMapping("/games/{gameId}/board/create")
    Board newBoard(@PathVariable long gameId){
        return boardService.getBoard(gameId);
    }

}