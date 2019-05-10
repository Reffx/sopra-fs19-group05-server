package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
    }
    @Test
    public void getGame() throws Exception {

        this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUser1\", \"password\": \"testPassword\"}"))
                .andExpect(status().is(201)).andDo(print())
                .andExpect(jsonPath("$.username", equalTo("testUser1")));

        this.mvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"player1\": {\"id\": \"1\", \"username\": \"testUser1\"}, \"gameMode\": \"NORMAL\",\"creationTime\": null,\"isPlaying\":false}"));

        this.mvc.perform(get("/games/11"))
                .andExpect(status().is(302))
                .andExpect(jsonPath("$.gameMode", equalTo("NORMAL")));

        this.mvc.perform(get("/games/25"))
                .andExpect(status().is(404));

        userRepository.deleteAll();
        gameRepository.deleteAll();
        workerNormalRepository.deleteAll();
        playerRepository.deleteAll();
    } */






}
