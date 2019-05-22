package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs19.constant.GodCards;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.Board;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.Player;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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


    public Game setUpTestNormalGame(){
        userRepository.deleteAll();
        gameRepository.deleteAll();


        //create 2 Users

        //User1
        User testUser1 = new User();
        testUser1.setUsername("testUsername1");
        testUser1.setPassword("test");
        testUser1.setBirthday("16.03.1994");

        User createdUser1 = userService.createUser(testUser1);
        User onlineUser1 = userService.checkUser(createdUser1);


        //User2
        User testUser3 = new User();
        testUser3.setUsername("testUsername3");
        testUser3.setPassword("test");
        testUser3.setBirthday("16.03.1994");

        User createdUser3 = userService.createUser(testUser3);

        User onlineUser3 = userService.checkUser(createdUser3);

        //create a Game with User 1

        Game testGame1 = new Game();
        Player playerOne = new Player();
        testGame1.setPlayer1(playerOne);
        testGame1.setGameMode(GameMode.NORMAL);
        testGame1.setIsPlaying(false);

        playerOne.setId(createdUser1.getId());
        playerOne.setUsername(createdUser1.getUsername());
        Game createdGame1 = gameService.createGame(testGame1);


        //Join the created game with createdUser2 (Player2)
        Game tempGame1 = gameService.joinLobby(createdUser3.getId(), createdGame1.getId()).getBody();

        Player playerTwo = tempGame1.getPlayer2();

        Board tempBoard = boardService.getBoard(tempGame1.getId());

        return tempGame1;
    }

    public Game setUpTestGodGame(){
        userRepository.deleteAll();
        gameRepository.deleteAll();


        //create 2 Users

        //User1
        User testUser1 = new User();
        testUser1.setUsername("testUsername1");
        testUser1.setPassword("test");
        testUser1.setBirthday("16.03.1994");

        User createdUser1 = userService.createUser(testUser1);
        User onlineUser1 = userService.checkUser(createdUser1);


        //User2
        User testUser3 = new User();
        testUser3.setUsername("testUsername3");
        testUser3.setPassword("test");
        testUser3.setBirthday("16.03.1994");

        User createdUser3 = userService.createUser(testUser3);

        User onlineUser3 = userService.checkUser(createdUser3);

        //create a Game with User 1

        Game testGame1 = new Game();
        Player playerOne = new Player();
        testGame1.setPlayer1(playerOne);
        testGame1.setGameMode(GameMode.GOD);
        testGame1.setIsPlaying(false);

        playerOne.setId(createdUser1.getId());
        playerOne.setUsername(createdUser1.getUsername());
        Game createdGame1 = gameService.createGame(testGame1);


        //Join the created game with createdUser2 (Player2)
        Game tempGame1 = gameService.joinLobby(createdUser3.getId(), createdGame1.getId()).getBody();

        Player playerTwo = tempGame1.getPlayer2();

        Board tempBoard = boardService.getBoard(tempGame1.getId());

        return tempGame1;
    }

    @Test
    public void placeWorker(){

        gameRepository.deleteAll();
        userRepository.deleteAll();
        boardRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();


        Assert.assertNull(gameRepository.getById(1));
        Assert.assertNull(playerRepository.findByUsername("testPlayer91"));

        Game createdGame = setUpTestNormalGame();
        Player player1 = createdGame.getPlayer1();

        // init board


        workerService.placeWorker(createdGame.getId(),player1.getWorker1().getWorkerId(), 6).getBody();

        Game gameTest = gameRepository.getById(createdGame.getId());

        Assert.assertEquals(playerRepository.findByUsername("testUsername1").getWorker1().getPosition(), 6);
        Assert.assertEquals(gameTest.getPlayer1().getWorker1().getPosition(), 6);
        Assert.assertNotNull(boardService.getField(playerRepository.findByUsername("testUsername1").getWorker1().getPosition(), gameTest.getId()));
        Assert.assertEquals(playerRepository.getById(player1.getId()).getWorker1().getPosition(), 6);
        Assert.assertEquals(boardService.getField(playerRepository.findByUsername("testUsername1").getWorker1().getPosition(), gameTest.getId()).getOccupier().getWorkerId(), player1.getWorker1().getWorkerId());

        Assert.assertEquals(gameRepository.getById(gameTest.getId()).getGameStatus(), GameStatus.Start);
        Assert.assertNotEquals(gameRepository.getById(gameTest.getId()).getGameStatus(), GameStatus.Move2);

        gameRepository.deleteAll();
        userRepository.deleteAll();
        boardRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();

    }

    @Test
    public void moveTo(){
        gameRepository.deleteAll();
        userRepository.deleteAll();
        boardRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();
        Assert.assertNull(gameRepository.getById(1));
        Assert.assertNull(workerNormalRepository.findById(1));
        Assert.assertNull(playerRepository.findByUsername("testPlayer9"));

        Game createdGame = setUpTestNormalGame();
        Player player1 = createdGame.getPlayer1();

        workerService.placeWorker(createdGame.getId(),player1.getWorker1().getWorkerId(), 6).getBody();
        workerService.moveTo(createdGame.getId(),player1.getWorker1().getWorkerId(), 9);

        Game gameTest = gameRepository.getById(createdGame.getId());

        Assert.assertEquals(playerRepository.findByUsername("testUsername1").getWorker1().getPosition(), 9);
        Assert.assertNotEquals(gameTest.getPlayer1().getWorker1().getPosition(), 6);
        Assert.assertEquals(gameTest.getPlayer1().getWorker1().getPosition(), 9);
        Assert.assertEquals(boardService.getField(6, gameTest.getId()).getOccupier(), null );

        gameRepository.deleteAll();
        userRepository.deleteAll();
        boardRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();

    }

    @Test
    public void build(){
        gameRepository.deleteAll();
        userRepository.deleteAll();
        boardRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();

        // set up test game
        Game tempGame1 = setUpTestNormalGame();
        Player playerOne = tempGame1.getPlayer1();
        playerOne.getWorker1().setPosition(4);
        //test the highlightFieldBuild
        workerService.highlightFieldBuild(playerOne.getWorker1().getPosition(), tempGame1.getId());
        Assert.assertNotNull(workerService.highlightFieldBuild(playerOne.getWorker1().getPosition(), tempGame1.getId()));
        Assert.assertNotEquals(workerService.highlightFieldBuild(playerOne.getWorker1().getPosition(), tempGame1.getId()).getBody(), 1);

        workerService.highlightFieldMove(5,tempGame1.getId());
        Assert.assertNotNull(workerService.highlightFieldMove(5,tempGame1.getId()));
        workerService.placeWorker(tempGame1.getId(),playerOne.getWorker1().getWorkerId(), 5 );
        //place worker from second player otherwise winning condition check will raise an error
        workerService.placeWorker(tempGame1.getId(), tempGame1.getPlayer2().getWorker1().getWorkerId(),2);
        workerService.moveTo(tempGame1.getId(),playerOne.getWorker1().getWorkerId(), 6);
        workerService.build(tempGame1.getId(), 5, playerOne.getWorker1().getWorkerId() );
        Assert.assertEquals(boardService.getField(5,tempGame1.getId()).getHeight(), 1);

        gameRepository.deleteAll();
        userRepository.deleteAll();
        boardRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();
    }

    @Test
    public void highlightMove(){
        gameRepository.deleteAll();
        userRepository.deleteAll();
        boardRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();
        //setting up the a normal game without god cards
        Game testGame = setUpTestNormalGame();

        //checking the highlight function from different positions
        ArrayList<Integer> checkList = new ArrayList<Integer>();
        checkList.add(0);
        checkList.add(2);
        checkList.add(5);
        checkList.add(6);
        checkList.add(7);

        //place worker on position 1
        workerService.placeWorker(testGame.getId(), testGame.getPlayer1().getWorker1().getWorkerId(),1);
        Assert.assertEquals(workerService.highlightFieldMove(1, testGame.getId()).getBody(), checkList);

        //move with worker on position 6
        workerService.moveTo(testGame.getId(), testGame.getPlayer1().getWorker1().getWorkerId(),6);
        checkList.clear();
        checkList.add(0);
        checkList.add(1);
        checkList.add(2);
        checkList.add(5);
        checkList.add(7);
        checkList.add(10);
        checkList.add(11);
        checkList.add(12);

        Assert.assertEquals(workerService.highlightFieldMove(6, testGame.getId()).getBody(), checkList);
    }

    @Test
    public void highlightBuild(){
        gameRepository.deleteAll();
        userRepository.deleteAll();
        boardRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();
        //setting up the a normal game without god cards
        Game testGame = setUpTestNormalGame();

        //checking the highlight function from different positions
        ArrayList<Integer> checkList = new ArrayList<Integer>();
        workerService.placeWorker(testGame.getId(), testGame.getPlayer1().getWorker1().getWorkerId(),1);
        workerService.placeWorker(testGame.getId(), testGame.getPlayer2().getWorker1().getWorkerId(),2);
        workerService.moveTo(testGame.getId(), testGame.getPlayer1().getWorker1().getWorkerId(),0);

        checkList.add(1);
        checkList.add(5);
        checkList.add(6);

        Assert.assertEquals(workerService.highlightFieldBuild(0, testGame.getId()).getBody(), checkList);

        //building on field 1 to level 4 --> not possible to build further
        workerService.build(testGame.getId(),1, testGame.getPlayer1().getWorker1().getWorkerId());
        workerService.build(testGame.getId(),1, testGame.getPlayer1().getWorker1().getWorkerId());
        workerService.build(testGame.getId(),1, testGame.getPlayer1().getWorker1().getWorkerId());
        workerService.build(testGame.getId(),1, testGame.getPlayer1().getWorker1().getWorkerId());
        // removing field 1 from check list
        checkList.remove(0);
        Assert.assertEquals(workerService.highlightFieldBuild(0, testGame.getId()).getBody(), checkList);

    }
    @Test
    public void highlightPrometheusAthena(){
        gameRepository.deleteAll();
        userRepository.deleteAll();
        boardRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();

        Game currentGame = setUpTestGodGame();

        //assign god card prometheus and athena
        gameService.assignGodCard("Prometheus", currentGame.getPlayer1().getId());

        System.out.println("GodCard Player1: "+currentGame.getPlayer1().getWorker1().getGodCard());
        gameService.assignGodCard("InactiveAthena", currentGame.getPlayer2().getId());


        ArrayList<Integer> checkList = new ArrayList<Integer>();
        currentGame.setGameStatus(GameStatus.Move1);
        gameRepository.save(currentGame);
        workerService.placeWorker(currentGame.getId(), currentGame.getPlayer1().getWorker1().getWorkerId(),1);
        workerService.placeWorker(currentGame.getId(), currentGame.getPlayer1().getWorker2().getWorkerId(),10);
        workerService.placeWorker(currentGame.getId(), currentGame.getPlayer2().getWorker1().getWorkerId(),2);
        workerService.placeWorker(currentGame.getId(), currentGame.getPlayer2().getWorker2().getWorkerId(),13);
        workerService.moveTo(currentGame.getId(), currentGame.getPlayer1().getWorker1().getWorkerId(),0);
        workerService.build(currentGame.getId(),1, currentGame.getPlayer1().getWorker1().getWorkerId());
        workerService.moveTo(currentGame.getId(), currentGame.getPlayer2().getWorker1().getWorkerId(),1);

        Assert.assertEquals(boardService.getField(1, currentGame.getId()).getHeight(), 1);

        workerService.moveTo(currentGame.getId(), currentGame.getPlayer2().getWorker1().getWorkerId(),2);

        checkList.add(5);
        checkList.add(6);
        //manually activate athena
        gameService.assignGodCard("InactiveAthena", currentGame.getPlayer2().getId());
        gameService.assignGodCard("Prometheus", currentGame.getPlayer1().getId());
        Assert.assertEquals(workerService.highlightFieldMove(0,  gameRepository.getById(currentGame.getId()).getId()).getBody(), checkList);
        checkList.clear();
        checkList.add(1);
        checkList.add(5);
        checkList.add(6);
        Assert.assertEquals(workerService.highlightFieldBuild(0, currentGame.getId()).getBody(), checkList);
        workerService.moveTo( gameRepository.getById(currentGame.getId()).getId(),  gameRepository.getById(currentGame.getId()).getPlayer1().getWorker1().getWorkerId(),5);


    }



}



