package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.controller.DuplicateException;

import ch.uzh.ifi.seal.soprafs19.entity.Board;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.Player;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.*;

import org.junit.Assert;
import org.junit.Ignore;
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


import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// The complex test goes from the start of the game (user creation/login) over to the creation of a lobby/joining an existing lobby
//After checking if a lobby is full the whole Game object will be tested for completeness and after that we go over to the InGame
//Board object creation

@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
@AutoConfigureMockMvc
@Transactional
public class ComplexTest {


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
    private GameService gameService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private UserService userService;
    @Autowired
    private WorkerService workerService;
    @Autowired
    private BoardService boardService;

    //starting with testing user creation
    @Test
    @Ignore
    public void createUser() throws Exception {
        userRepository.deleteAll();
        playerRepository.deleteAll();
        gameRepository.deleteAll();

        //making a post request to the users endpoint to create a user, after that the same credentials are used to create a second user
        //expected HTTP status is 409
        this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUser1\", \"password\": \"testPassword1\"}"))
                .andExpect(status().is(201)).andDo(print())
                .andExpect(jsonPath("$.username", equalTo("testUser1")));
        this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testUser1\", \"password\": \"testPassword1\"}"))
                .andExpect(status().is(409));

        // asserting that the user object from the post request is saved correctly in the user repo
        Assert.assertEquals(userRepository.findByUsername("testUser1").getUsername(), "testUser1");
        Assert.assertNotNull(userRepository.findByPassword("testPassword1").getId());
        Assert.assertEquals(userRepository.findByUsername("testUser1").getPassword(), "testPassword1");
        System.out.print(userRepository.findByUsername("testUser1").getId());
        //Assert.assertEquals(userRepository.findById(1).getUsername(), "testUser1");


        //now we test if the createUser method without performing a post method
        //Assert.assertNotNull(userRepository.findById(1));

        userRepository.deleteAll();
        playerRepository.deleteAll();
        gameRepository.deleteAll();

    }


    // 1. Complex Unit Test --> test the createGame function (trying to set up 2 games with the same player, change username try again, change id and try again)
    @Test(expected = DuplicateException.class)
    public void complexUnitTest(){

        userRepository.deleteAll();
        gameRepository.deleteAll();
        playerRepository.deleteAll();

        Assert.assertNull(gameRepository.getById(1));
        Game testGame = new Game();
        Player player1 = new Player();
        long player1Id = 50;
        player1.setId(player1Id);
        player1.setUsername("TestPlayer50");
        testGame.setPlayer1(player1);
        testGame.setGameMode(GameMode.NORMAL);
        testGame.setIsPlaying(false);

        //test the if values are set correctly in player1 object

        Assert.assertEquals(player1.getUsername(), "TestPlayer50");
        Assert.assertNull(player1.getColor());
        Assert.assertEquals(player1.getId(), testGame.getPlayer1().getId());
        Assert.assertFalse(player1.getStatus());
        Assert.assertEquals(testGame.getGameMode(), GameMode.NORMAL);
        Assert.assertNull(testGame.getPlayer2());

        Game createdGame = gameService.createGame(testGame);


        //check if all values are still set correctly after the createGame function is triggered
        Assert.assertNull(createdGame.getPlayer2());
        Assert.assertEquals(createdGame.getPlayer1().getUsername(), "TestPlayer50");
        Assert.assertEquals(createdGame.getPlayer1().getId(), player1.getId());

        //try to create a game with the same player
        gameService.createGame(testGame);

        //change id and try to create a new game
        long newPlayerId = 27;
        player1.setId(newPlayerId);
        Assert.assertEquals(createdGame.getPlayer1().getId(), player1.getId() );

        gameService.createGame(testGame);

        player1.setUsername("BestPlayer69");

        Assert.assertEquals(createdGame.getPlayer1().getUsername(), player1.getUsername());

        gameService.createGame(testGame);


        userRepository.deleteAll();
        gameRepository.deleteAll();
        playerRepository.deleteAll();

    }


    //2. Complex Integration Test --> test the moving functionality
    @Test
    public void complexIntegrationTest(){
        userRepository.deleteAll();
        gameRepository.deleteAll();
        playerRepository.deleteAll();

        //create 2 Users

        //User1
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("test");
        testUser.setBirthday("16.03.1994");

        User createdUser = userService.createUser(testUser);
        Assert.assertEquals(createdUser.getStatus(), UserStatus.OFFLINE);

        User onlineUser = userService.checkUser(createdUser);
        Assert.assertEquals(onlineUser.getStatus(),UserStatus.ONLINE);

        User offlineUser = userService.logoutUser(onlineUser);
        Assert.assertEquals(offlineUser.getStatus(),UserStatus.OFFLINE);

        //User2
        User testUser2 = new User();
        testUser2.setUsername("testUsername2");
        testUser2.setPassword("test");
        testUser2.setBirthday("16.03.1994");

        User createdUser2 = userService.createUser(testUser2);
        Assert.assertEquals(createdUser2.getStatus(), UserStatus.OFFLINE);

        User onlineUser2 = userService.checkUser(createdUser);
        Assert.assertEquals(onlineUser2.getStatus(),UserStatus.ONLINE);

        User offlineUser2 = userService.logoutUser(onlineUser2);
        Assert.assertEquals(offlineUser2.getStatus(),UserStatus.OFFLINE);

        //create a Game with User 1

        Game testGame = new Game();
        Player player1 = new Player();
        testGame.setPlayer1(player1);
        testGame.setGameMode(GameMode.NORMAL);
        testGame.setIsPlaying(false);

        player1.setId(createdUser.getId());
        player1.setUsername(createdUser.getUsername());
        Game createdGame = gameService.createGame(testGame);

        Assert.assertEquals(createdGame.getPlayer1().getUsername(), testUser.getUsername());
        Assert.assertEquals(createdGame.getSize(),1);

        //Join the created game with createdUser2 (Player2)
        Game tempGame = gameService.joinLobby(createdUser2.getId(), createdGame.getId()).getBody();

        Player player2 = tempGame.getPlayer2();

        Assert.assertNotNull(tempGame.getPlayer2());
        Assert.assertEquals(tempGame.getPlayer2().getUsername(), createdUser2.getUsername());
        Assert.assertNotNull(tempGame.getPlayer1());
        Assert.assertNotNull(tempGame.getPlayer2());
        Assert.assertNotNull(tempGame.getPlayer1().getWorker1());
        Assert.assertNotNull(tempGame.getPlayer1().getWorker2());
        Assert.assertNotNull(tempGame.getPlayer2().getWorker1());
        Assert.assertNotNull(tempGame.getPlayer2().getWorker2());

        Assert.assertEquals(tempGame.getGameMode(), GameMode.NORMAL);
        Assert.assertEquals(tempGame.getGameStatus(), GameStatus.Start);
        Assert.assertEquals(userRepository.findByUsername(createdUser.getUsername()), createdUser);
        Assert.assertEquals(userRepository.findByUsername(createdUser2.getUsername()), createdUser2);
        Assert.assertEquals(tempGame.getSize(),2);


        // initialize board

        Board tempBoard = boardService.getBoard(tempGame.getId());

        //place worker1 of player1 on field 5

        workerService.placeWorker(tempGame.getId(),player1.getWorker1().getWorkerId(), 5);
        //updated place worker function --> update test
        Assert.assertEquals(tempGame.getGameStatus(), GameStatus.Start);
        Assert.assertNotNull(boardService.getField(5,tempGame.getId()).getOccupier());
        Assert.assertNotNull(boardRepository.findById(tempGame.getId()));
        Assert.assertEquals(boardService.getField(5,tempGame.getId()).getHeight(), 0);
        Assert.assertEquals(boardService.getField(5,tempGame.getId()).getOccupier(), player1.getWorker1());
        Assert.assertEquals(tempGame.getPlayer1().getWorker1().getPosition(), 5);
        Assert.assertFalse(tempGame.getPlayer1().getWorker1().getIsWinner());

        //Place Worker2 of Player1 on Field Number 6

        workerService.placeWorker(tempGame.getId(),player1.getWorker2().getWorkerId(), 6);
        Assert.assertEquals(tempGame.getGameStatus(), GameStatus.Start);
        Assert.assertNotNull(boardService.getField(6,tempGame.getId()).getOccupier());
        Assert.assertNotNull(boardRepository.findById(tempGame.getId()));
        Assert.assertEquals(boardService.getField(6,tempGame.getId()).getHeight(), 0);
        Assert.assertEquals(boardService.getField(6,tempGame.getId()).getOccupier(), player1.getWorker2());
        Assert.assertEquals(tempGame.getPlayer1().getWorker2().getPosition(), 6);
        Assert.assertFalse(tempGame.getPlayer1().getWorker2().getIsWinner());

        //Place Worker1 of Player2 on Field Number 12

        workerService.placeWorker(tempGame.getId(), player2.getWorker1().getWorkerId(), 12);
        Assert.assertEquals(tempGame.getGameStatus(), GameStatus.Start);
        Assert.assertNotNull(boardService.getField(12,tempGame.getId()).getOccupier());
        Assert.assertNotNull(boardRepository.findById(tempGame.getId()));
        Assert.assertEquals(boardService.getField(12,tempGame.getId()).getHeight(), 0);
        Assert.assertEquals(boardService.getField(12,tempGame.getId()).getOccupier(), player2.getWorker1());
        Assert.assertEquals(tempGame.getPlayer2().getWorker1().getPosition(), 12);
        Assert.assertFalse(tempGame.getPlayer2().getWorker1().getIsWinner());

        // Place Worker 2 of Player2 on FieldNumber 20

        workerService.placeWorker(tempGame.getId(), player2.getWorker2().getWorkerId(), 20);
        Assert.assertEquals(tempGame.getGameStatus(), GameStatus.Start);
        Assert.assertNotNull(boardService.getField(20,tempGame.getId()).getOccupier());
        Assert.assertNotNull(boardRepository.findById(tempGame.getId()));
        Assert.assertEquals(boardService.getField(20,tempGame.getId()).getHeight(), 0);
        Assert.assertEquals(boardService.getField(20,tempGame.getId()).getOccupier(), player2.getWorker2());
        Assert.assertEquals(tempGame.getPlayer2().getWorker2().getPosition(), 20);
        Assert.assertFalse(tempGame.getPlayer2().getWorker2().getIsWinner());

        // Both players placed their workers on the board


        //Now Player 1 can move one of his workers

        workerService.moveTo(tempGame.getId(),player1.getWorker1().getWorkerId(), 1);
        //check for updated Game Status Moving --> Building
        Assert.assertEquals(tempGame.getGameStatus(), GameStatus.Start);
        Assert.assertNotNull(boardService.getField(1,tempGame.getId()).getOccupier());
        //old position shouldn't have a worker object on it anymore
        Assert.assertNull(boardService.getField(5, tempGame.getId()).getOccupier());
        Assert.assertNotNull(boardRepository.findById(tempGame.getId()));
        Assert.assertEquals(boardService.getField(1,tempGame.getId()).getHeight(), 0);
        Assert.assertEquals(boardService.getField(1,tempGame.getId()).getOccupier(), player1.getWorker1());
        Assert.assertEquals(tempGame.getPlayer1().getWorker1().getPosition(), 1);

        //Build with Worker1
        workerService.build(tempGame.getId(), 2, player1.getWorker1().getWorkerId() );
        Assert.assertEquals(boardService.getField(2,tempGame.getId()).getHeight(), 1);
        //FieldNum 2 has building level 1

        //Move Worker 2 of Player 2 to a surrounding field (current position is 20)
        workerService.moveTo(tempGame.getId(),player2.getWorker2().getWorkerId(), 15);
        //from this position both build and move highlighting functions should return the same result
        Assert.assertEquals(workerService.highlightFieldBuild(15,tempGame.getId()).getBody(), workerService.highlightFieldMove(15, tempGame.getId()).getBody());
        workerService.build(tempGame.getId(), 20, player2.getWorker2().getWorkerId());
        Assert.assertEquals(boardService.getField(20, tempGame.getId()).getHeight(), 1);
        Assert.assertEquals(tempGame.getGameStatus(), GameStatus.Start);

        //Move with Worker 1 from Player 1 from Field 1 to Field 7

        workerService.moveTo(tempGame.getId(),player1.getWorker1().getWorkerId(), 7);
        workerService.build(tempGame.getId(),2, player1.getWorker1().getWorkerId());

        Assert.assertEquals(boardService.getField(2,tempGame.getId()).getHeight(), 2);

        Assert.assertFalse(player1.getWorker1().getIsWinner());

        workerService.moveTo(tempGame.getId(), player1.getWorker2().getWorkerId(), 5);

        Assert.assertFalse(player1.getWorker1().getIsWinner());

        workerService.build(tempGame.getId(), 1, player1.getWorker2().getWorkerId());
        Assert.assertEquals(boardService.getField(1,tempGame.getId()).getHeight(), 1);

        //Move with Worker1 (Player1) from Field 7 to Field 6
        workerService.moveTo(tempGame.getId(), player1.getWorker1().getWorkerId(), 6);

        Assert.assertFalse(player1.getWorker1().getIsWinner());

        workerService.build(tempGame.getId(),1, player1.getWorker1().getWorkerId());
        Assert.assertEquals(boardService.getField(1,tempGame.getId()).getHeight(), 2);

        workerService.moveTo(tempGame.getId(), player1.getWorker2().getWorkerId(), 5);
        workerService.build(tempGame.getId(),0, player1.getWorker2().getWorkerId());
        Assert.assertEquals(boardService.getField(0,tempGame.getId()).getHeight(), 1);

        //Move with Worker 1 (Player1) from Field 6 to Field 1 and build on Field 2 --> Building level changes to 3
        //highlighting function for building should be different from the highlighting function for moving since we current height is 0
        //and there are surrounding fields with building level >1
        Assert.assertNotEquals(workerService.highlightFieldBuild(6,tempGame.getId()).getBody(), workerService.highlightFieldMove(6, tempGame.getId()).getBody());
        workerService.moveTo(tempGame.getId(), player1.getWorker1().getWorkerId(), 0);
        Assert.assertFalse(player1.getWorker1().getIsWinner());
        Assert.assertNull(boardService.getField(6, tempGame.getId()).getOccupier());
        Assert.assertNotEquals(gameRepository.getById(tempGame.getId()).getGameStatus(), GameStatus.Move1);
        Assert.assertNotEquals(gameRepository.getById(tempGame.getId()).getGameStatus(), GameStatus.Move2);
        Assert.assertNotEquals(gameRepository.getById(tempGame.getId()).getGameStatus(), GameStatus.Build2);
        Assert.assertEquals(gameRepository.getById(tempGame.getId()).getGameStatus(), GameStatus.Start);


        //Move with Worker1 (Player1) from Field 0 to Field 1
        workerService.moveTo(tempGame.getId(), player1.getWorker1().getWorkerId(), 1);
        Assert.assertFalse(player1.getWorker1().getIsWinner());
        workerService.build(tempGame.getId(), 2, player1.getWorker1().getWorkerId());


        Assert.assertEquals(boardService.getField(2,tempGame.getId()).getHeight(), 3);
        Assert.assertFalse(player1.getWorker1().getIsWinner());
        // highlighting of building and moving possibilities are the same because current position level of worker is 2
        Assert.assertEquals(workerService.highlightFieldBuild(1,tempGame.getId()).getBody(), workerService.highlightFieldMove(1, tempGame.getId()).getBody());
        workerService.moveTo(tempGame.getId(), player1.getWorker1().getWorkerId(), 2);

        //check if isWinner is true for Worker 1 of Player 1
        Assert.assertTrue(player1.getWorker1().getIsWinner());



        userRepository.deleteAll();
        gameRepository.deleteAll();
        playerRepository.deleteAll();
    }

    //complex rest test
    //starting with testing game creation

    @Test
    @Ignore
    public void createGameErr() throws Exception {
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
    //check if a player can join a lobby twice
    @Test
    @Ignore
    public void joinLobbyErr() throws Exception {
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
                .content("{\"player1\": {\"id\": \"8\", \"username\": \"testUser1\"}, \"gameMode\": \"NORMAL\",\"creationTime\": null,\"isPlaying\":false}"))
                .andExpect(status().is(201)).andDo(print())
                .andExpect(jsonPath("$.gameMode", equalTo("NORMAL")));

        this.mvc.perform(put("/games/12/player")
                .contentType(MediaType.APPLICATION_JSON)
                .content("9"))
                .andExpect(status().is(200)).andDo(print())
                .andExpect(jsonPath("$.size", equalTo(2)));

        this.mvc.perform(put("/games/12/player")
                .contentType(MediaType.APPLICATION_JSON)
                .content("9"))
                .andExpect(status().is(409));

        userRepository.deleteAll();
        gameRepository.deleteAll();
        playerRepository.deleteAll();

    }


}

