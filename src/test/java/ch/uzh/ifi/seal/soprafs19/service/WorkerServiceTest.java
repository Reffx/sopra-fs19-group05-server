package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.*;
import ch.uzh.ifi.seal.soprafs19.controller.FullLobbyException;
import ch.uzh.ifi.seal.soprafs19.controller.NonExistentGameException;
import ch.uzh.ifi.seal.soprafs19.entity.Board;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.Player;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.*;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import ch.uzh.ifi.seal.soprafs19.controller.DuplicateException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @see ch.uzh.ifi.seal.soprafs19.service.WorkerService
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class WorkerServiceTest {


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


    @Test
    public void placeWorker(){

        gameRepository.deleteAll();


        Assert.assertNull(gameRepository.getById(1));
        Assert.assertNull(playerRepository.findByUsername("testPlayer91"));
        Game testGame = new Game();
        Player player1 = new Player();
        testGame.setPlayer1(player1);
        testGame.setGameMode(GameMode.NORMAL);
        testGame.setIsPlaying(false);
        long player1ID = 91;
        player1.setId(player1ID);
        player1.setUsername("testPlayer91");
        Game createdGame = gameService.createGame(testGame);

        // init board


        workerService.placeWorker(createdGame.getId(),player1.getWorker1().getWorkerId(), 6).getBody();

        Game gameTest = gameRepository.getById(createdGame.getId());

        Assert.assertEquals(playerRepository.findByUsername("testPlayer91").getWorker1().getPosition(), 6);
        Assert.assertEquals(gameTest.getPlayer1().getWorker1().getPosition(), 6);
        Assert.assertNotNull(boardService.getField(playerRepository.findByUsername("testPlayer91").getWorker1().getPosition(), gameTest.getId()));
        Assert.assertEquals(playerRepository.getById(player1ID).getWorker1().getPosition(), 6);
        Assert.assertEquals(boardService.getField(playerRepository.findByUsername("testPlayer91").getWorker1().getPosition(), gameTest.getId()).getOccupier().getWorkerId(), player1.getWorker1().getWorkerId());

        Assert.assertEquals(gameRepository.getById(gameTest.getId()).getGameStatus(), GameStatus.Move1);
        Assert.assertNotEquals(gameRepository.getById(gameTest.getId()).getGameStatus(), GameStatus.Move2);

        gameRepository.deleteAll();

    }

    @Test
    public void moveTo(){
        gameRepository.deleteAll();
        playerRepository.deleteAll();
        Assert.assertNull(gameRepository.getById(1));
        Assert.assertNull(workerNormalRepository.findById(1));
        Assert.assertNull(playerRepository.findByUsername("testPlayer9"));
        Game testGame = new Game();
        Player player1 = new Player();
        testGame.setPlayer1(player1);
        testGame.setGameMode(GameMode.NORMAL);
        testGame.setIsPlaying(false);
        long player1ID = 9;
        player1.setId(player1ID);
        player1.setUsername("testPlayer9");
        Game createdGame = gameService.createGame(testGame);


        workerService.placeWorker(createdGame.getId(),player1.getWorker1().getWorkerId(), 6).getBody();
        workerService.moveTo(createdGame.getId(),player1.getWorker1().getWorkerId(), 9);

        Game gameTest = gameRepository.getById(createdGame.getId());

        Assert.assertEquals(playerRepository.findByUsername("testPlayer9").getWorker1().getPosition(), 9);
        Assert.assertNotEquals(gameTest.getPlayer1().getWorker1().getPosition(), 6);
        Assert.assertEquals(gameTest.getPlayer1().getWorker1().getPosition(), 9);
        Assert.assertEquals(boardService.getField(6, gameTest.getId()).getOccupier(), null );

        userRepository.deleteAll();
        gameRepository.deleteAll();
    }

    @Test
    public void build(){
        userRepository.deleteAll();
        gameRepository.deleteAll();


        //create 2 Users

        //User1
        User testUser1 = new User();
        testUser1.setUsername("testUsername1");
        testUser1.setPassword("test");
        testUser1.setBirthday("16.03.1994");

        User createdUser1 = userService.createUser(testUser1);
        Assert.assertEquals(createdUser1.getStatus(), UserStatus.OFFLINE);

        User onlineUser1 = userService.checkUser(createdUser1);
        Assert.assertEquals(onlineUser1.getStatus(),UserStatus.ONLINE);

        User offlineUser1 = userService.logoutUser(onlineUser1);
        Assert.assertEquals(offlineUser1.getStatus(),UserStatus.OFFLINE);

        //User2
        User testUser3 = new User();
        testUser3.setUsername("testUsername3");
        testUser3.setPassword("test");
        testUser3.setBirthday("16.03.1994");

        User createdUser3 = userService.createUser(testUser3);
        Assert.assertEquals(createdUser3.getStatus(), UserStatus.OFFLINE);

        User onlineUser3 = userService.checkUser(createdUser3);
        Assert.assertEquals(onlineUser3.getStatus(),UserStatus.ONLINE);

        User offlineUser3 = userService.logoutUser(onlineUser3);
        Assert.assertEquals(offlineUser3.getStatus(),UserStatus.OFFLINE);

        //create a Game with User 1

        Game testGame1 = new Game();
        Player playerOne = new Player();
        testGame1.setPlayer1(playerOne);
        testGame1.setGameMode(GameMode.NORMAL);
        testGame1.setIsPlaying(false);

        playerOne.setId(createdUser1.getId());
        playerOne.setUsername(createdUser1.getUsername());
        Game createdGame1 = gameService.createGame(testGame1);

        Assert.assertEquals(createdGame1.getPlayer1().getUsername(), testUser1.getUsername());
        Assert.assertEquals(createdGame1.getSize(),1);

        //Join the created game with createdUser2 (Player2)
        Game tempGame1 = gameService.joinLobby(createdUser3.getId(), createdGame1.getId()).getBody();

        Player playerTwo = tempGame1.getPlayer2();

        Board tempBoard = boardService.getBoard(tempGame1.getId());

        playerOne.getWorker1().setPosition(4);
        //test the highlightFieldBuild
        workerService.highlightFieldBuild(playerOne.getWorker1().getPosition(), tempGame1.getId());
        Assert.assertNotNull(workerService.highlightFieldBuild(playerOne.getWorker1().getPosition(), tempGame1.getId()));
        Assert.assertNotEquals(workerService.highlightFieldBuild(playerOne.getWorker1().getPosition(), tempGame1.getId()).getBody(), 1);

        workerService.highlightFieldMove(5,tempGame1.getId());
        Assert.assertNotNull(workerService.highlightFieldMove(5,tempGame1.getId()));

        workerService.build(tempGame1.getId(), 5);
        Assert.assertEquals(boardService.getField(5,tempGame1.getId()).getHeight(), 1);
    }

}

