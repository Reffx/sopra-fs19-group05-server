package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;

import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.entity.Board;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.Player;
import ch.uzh.ifi.seal.soprafs19.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;


import static org.hamcrest.core.IsEqual.equalTo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;



@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
@AutoConfigureMockMvc
@Transactional


public class GameControllerTest {

    @Autowired
    private MockMvc mvc;
    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;
    @Qualifier("playerRepository")
    @Autowired
    private PlayerRepository playerRepository;
    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;
    @Qualifier("workerNormalRepository")
    @Autowired
    private WorkerNormalRepository workerNormalRepository;
    @Qualifier("boardRepository")
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private PlayerService playerService;

    @Test
    public void createGame() throws Exception {
        userRepository.deleteAll();
        playerRepository.deleteAll();
        gameRepository.deleteAll();

        //making a post request to the games endpoint to create a game, after that the information are used to create a second game --> 409 error
        //expected HTTP status is 409
        this.mvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"player1\": {\"id\": \"2\", \"username\": \"testUser1\"}, \"gameMode\": \"NORMAL\",\"creationTime\": null,\"isPlaying\":false}"))
                .andExpect(status().is(201)).andDo(print())
                .andExpect(jsonPath("$.gameMode", equalTo("NORMAL")));
        this.mvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"player1\": {\"id\": \"2\", \"username\": \"testUser1\"}, \"gameMode\": \"NORMAL\",\"creationTime\": null,\"isPlaying\":false}"))
                .andExpect(status().is(409));

        userRepository.deleteAll();
        playerRepository.deleteAll();
    }
   /* @Test
    public void joinLobby() throws Exception {
        userRepository.deleteAll();
        playerRepository.deleteAll();
        gameRepository.deleteAll();

        //making a post request to the games endpoint to create a game, after that the information are used to create a second game --> 409 error
        //expected HTTP status is 409
        this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUser1\", \"password\": \"testPassword\"}"))
                .andExpect(status().is(201)).andDo(print())
                .andExpect(jsonPath("$.username", equalTo("testUser1")));

        this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUser2\", \"password\": \"testPassword\"}"))
                .andExpect(status().is(201)).andDo(print())
                .andExpect(jsonPath("$.username", equalTo("testUser2")));

        this.mvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"player1\": {\"id\": \"1\", \"username\": \"testUser1\"}, \"gameMode\": \"NORMAL\",\"creationTime\": null,\"isPlaying\":false}"))
                .andExpect(status().is(201)).andDo(print())
                .andExpect(jsonPath("$.gameMode", equalTo("NORMAL")));

        this.mvc.perform(put("/games/5/player")
                .contentType(MediaType.APPLICATION_JSON)
                .content("2"))
                .andExpect(status().is(200)).andDo(print())
                .andExpect(jsonPath("$.size", equalTo(2)));

        this.mvc.perform(put("/games/5/player")
                .contentType(MediaType.APPLICATION_JSON)
                .content("2"))
                .andExpect(status().is(409));

        userRepository.deleteAll();
        gameRepository.deleteAll();
        playerRepository.deleteAll();
    } */


    @Test
    public void getGame() throws Exception {

        //  create game to save into repository
        Game newGame = new Game();
        newGame.setGameMode(GameMode.NORMAL);
        Player player1 = new Player();
        player1.setId(1L);
        newGame.setPlayer1(player1);
        GameService gameService = new GameService(gameRepository,  playerService,  workerNormalRepository,  playerRepository,  boardRepository);
        gameService.createGame(newGame);

        // MVC test
        MvcResult result =  this.mvc.perform(get("/games/{gameId}", newGame.getId()))
                .andExpect(status().isFound())
                .andReturn();

        // check if the returned result is the same as that in boardrepository
        String content =  result.getResponse().getContentAsString();
        System.out.println(content);
        ObjectMapper mapper = new ObjectMapper();
        Game testGame = mapper.readValue(content, Game.class);

        assert(testGame.equals(newGame));
    }

}





