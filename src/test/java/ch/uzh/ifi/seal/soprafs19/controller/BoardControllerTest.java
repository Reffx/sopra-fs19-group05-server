package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.Application;

import ch.uzh.ifi.seal.soprafs19.entity.Board;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.Player;
import ch.uzh.ifi.seal.soprafs19.repository.*;
import ch.uzh.ifi.seal.soprafs19.service.BoardService;
import ch.uzh.ifi.seal.soprafs19.service.GameService;
import ch.uzh.ifi.seal.soprafs19.service.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.weaver.TypeFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;


import static org.hamcrest.core.IsEqual.equalTo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
@AutoConfigureMockMvc
@Transactional

public class BoardControllerTest {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private WorkerNormalRepository workerNormalRepository;
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    public void newBoard() throws Exception {

        //  create game to save into repository
        Game newGame = new Game();
        newGame.setId(1L);
        Player player1 = new Player();
        player1.setId(1L);
        newGame.setPlayer1(player1);
        GameService gameService = new GameService(gameRepository,  playerService,  workerNormalRepository,  playerRepository,  boardRepository);
        gameService.createGame(newGame);

        // create newBoard to be returned
        Board testBoard = new Board();
        testBoard.setId(1L);
        boardRepository.save(testBoard);

        // MVC test
        MvcResult result =  this.mvc.perform(get("/games/{gameId}/board/create", 1L))
                .andExpect(status().isOk())
                .andReturn();

        // check if the returned result is the same as that in boardrepository
        String content =  result.getResponse().getContentAsString();
        System.out.println(content);
        ObjectMapper mapper = new ObjectMapper();
        Board newBoard = mapper.readValue(content, Board.class);
        assert(newBoard.equals(testBoard));
    }
}
