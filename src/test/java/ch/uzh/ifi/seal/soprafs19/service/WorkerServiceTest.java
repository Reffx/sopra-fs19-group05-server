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
        Assert.assertNull(gameRepository.getById(1));
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

        boardService.getBoard(createdGame.getId());

        /*workerService.placeWorker(createdGame.getId(), createdGame.getPlayer1().getWorker1().getWorkerId(), 6);

        Assert.assertEquals(workerNormalRepository.findById(player1.getWorker1().getWorkerId()).getPosition(), 6);
        Assert.assertNotNull(boardService.getField(createdGame.getPlayer1().getWorker1().getPosition(), createdGame.getId()));
        */
    }

}

