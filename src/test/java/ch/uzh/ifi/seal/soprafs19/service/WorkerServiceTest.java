package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.Color;
import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs19.constant.GodCards;
import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.controller.FullLobbyException;
import ch.uzh.ifi.seal.soprafs19.controller.NonExistentGameException;
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

    }

}

