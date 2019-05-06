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
import ch.uzh.ifi.seal.soprafs19.repository.WorkerNormalRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.repository.PlayerRepository;
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
 * @see ch.uzh.ifi.seal.soprafs19.service.GameService
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class GameServiceTest {


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

    @Autowired
    private GameService gameService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private UserService userService;
    @Autowired
    private WorkerService workerService;

    @Test
    public void createGame(){
        Assert.assertNull(gameRepository.getById(1));
        Game testGame = new Game();
        Player player1 = new Player();
        testGame.setPlayer1(player1);
        testGame.setGameMode(GameMode.NORMAL);
        testGame.setIsPlaying(false);
        long player1ID = 14;
        player1.setId(player1ID);
        player1.setUsername("testPlayer");
        Game createdGame = gameService.createGame(testGame);

        Assert.assertNotNull(gameRepository.findByGameMode(GameMode.NORMAL));
        Assert.assertNotNull(createdGame.getPlayer1());
        Assert.assertNotNull(createdGame.getPlayer1().getWorker1());
        Assert.assertNotNull(createdGame.getPlayer1().getWorker2());

        gameRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();
    }

    @Test(expected = DuplicateException.class)
    public void createGameErr(){
        gameRepository.deleteAll();
        Game testGame = new Game();
        Player testplayer1 = new Player();
        testGame.setPlayer1(testplayer1);
        testGame.setGameMode(GameMode.NORMAL);
        testGame.setIsPlaying(false);
        long player1ID = 15;
        testplayer1.setId(player1ID);
        testplayer1.setUsername("testPlayer1");
        gameService.createGame(testGame);

        //create second game instance with the same player Id --> duplicate exception gets raised
        Game testGame2 = new Game();
        Player testplayer2 = new Player();
        testGame2.setPlayer1(testplayer2);
        testGame2.setGameMode(GameMode.NORMAL);
        testGame2.setIsPlaying(false);
        testplayer2.setId(player1ID);
        testplayer2.setUsername("testPlayer1");
        gameService.createGame(testGame2);
        playerRepository.deleteAll();
        gameRepository.deleteAll();
        workerNormalRepository.deleteAll();
    }

    @Test
    public void getGames(){
        gameRepository.deleteAll();
        Assert.assertNull(gameRepository.getById(1));
        Game testGame = new Game();
        Player testplayer1 = new Player();
        testGame.setPlayer1(testplayer1);
        testGame.setGameMode(GameMode.NORMAL);
        testGame.setIsPlaying(false);
        long player1ID = 2;
        testplayer1.setId(player1ID);
        testplayer1.setUsername("testPlayer1");
        Game createdGame = gameService.createGame(testGame);

        Assert.assertNotNull(gameRepository.findByGameMode(GameMode.NORMAL));
        Assert.assertNotNull(createdGame.getPlayer1());
        Assert.assertNotNull(createdGame.getPlayer1().getWorker1());
        Assert.assertNotNull(createdGame.getPlayer1().getWorker2());

        Game testGame2 = new Game();
        Player testplayer2 = new Player();
        testGame2.setPlayer1(testplayer2);
        testGame2.setGameMode(GameMode.NORMAL);
        testGame2.setIsPlaying(false);
        long testplayer2Id = 2;
        testplayer2.setId(testplayer2Id);
        testplayer2.setUsername("testPlayer2");
        Game createdGame2 = gameService.createGame(testGame2);

        Assert.assertNotNull(gameRepository.findByGameMode(GameMode.NORMAL));
        Assert.assertNotNull(createdGame2.getPlayer1());
        Assert.assertNotNull(createdGame2.getPlayer1().getWorker1());
        Assert.assertNotNull(createdGame2.getPlayer1().getWorker2());

        gameRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();
    }

    @Test
    public void getModeGames(){
        Assert.assertNull(gameRepository.getById(1));
        Game testGame = new Game();
        Player player1 = new Player();
        testGame.setPlayer1(player1);
        testGame.setGameMode(GameMode.NORMAL);
        testGame.setIsPlaying(false);
        long player1ID = 72;
        player1.setId(player1ID);
        player1.setUsername("testPlayer72");
        Game createdGame = gameService.createGame(testGame);
        Assert.assertNotNull(gameRepository.findByGameMode(GameMode.NORMAL));
        Assert.assertEquals(createdGame.getPlayer1(), player1);

        Iterable NormalModeGame = gameService.getModeGames(GameMode.NORMAL).getBody();
        Assert.assertNotNull(NormalModeGame);

        gameRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test(expected = NullPointerException.class)
    public void getModeGamesErr(){
        gameRepository.deleteAll();
        Assert.assertNull(gameRepository.getById(1));
        Game testGame = new Game();
        Player testplayer1 = new Player();
        testGame.setPlayer1(testplayer1);
        testGame.setGameMode(GameMode.NORMAL);
        testGame.setIsPlaying(false);
        Game createdGame = gameService.createGame(testGame);
        Assert.assertNotNull(gameRepository.findByGameMode(GameMode.NORMAL));
        Iterable NormalModeGames = gameService.getModeGames(GameMode.NORMAL).getBody();
        Assert.assertNotNull(NormalModeGames);
        gameRepository.deleteById(createdGame.getId());
        gameRepository.deleteById(createdGame.getId());

        gameRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();
        userRepository.deleteAll();

    }


    @Test
    public void getGame(){
        gameRepository.deleteAll();
        Assert.assertNull(gameRepository.getById(1));
        Game testGame = new Game();
        Player testplayer3 = new Player();
        testGame.setPlayer1(testplayer3);
        testGame.setGameMode(GameMode.NORMAL);
        testGame.setIsPlaying(false);
        long player1ID = 4;
        testplayer3.setId(player1ID);
        testplayer3.setUsername("testPlayer3");
        Game createdGame = gameService.createGame(testGame);

        Assert.assertNotNull(gameRepository.findByGameMode(GameMode.NORMAL));
        Assert.assertNotNull(createdGame.getPlayer1());
        Assert.assertNotNull(createdGame.getPlayer1().getWorker1());
        Assert.assertNotNull(createdGame.getPlayer1().getWorker2());

        //check if foundGame equals the created game
        Game foundGame = gameRepository.getById(createdGame.getId());
        Assert.assertNotNull(foundGame);
        Assert.assertEquals(createdGame, foundGame);

        gameRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();
    }

    @Test(expected = NonExistentGameException.class)
    public void getGameErr(){
        gameRepository.deleteAll();
        Assert.assertNull(gameRepository.getById(1));
        Game testGame = new Game();
        Player testplayer = new Player();
        testGame.setPlayer1(testplayer);
        testGame.setGameMode(GameMode.NORMAL);
        testGame.setIsPlaying(false);
        long player1ID = 0;
        testplayer.setId(player1ID);
        testplayer.setUsername("testPlayerx");
        Game createdGame = gameService.createGame(testGame);

        Assert.assertNotNull(gameRepository.findByGameMode(GameMode.NORMAL));
        Assert.assertNotNull(createdGame.getPlayer1());
        Assert.assertNotNull(createdGame.getPlayer1().getWorker1());
        Assert.assertNotNull(createdGame.getPlayer1().getWorker2());

        gameRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();
        gameService.getGame(0);
    }


    @Test
    public void joinLobby(){
        Assert.assertNull(gameRepository.getById(1));
        Game testGame = new Game();
        Player player1 = new Player();
        long player1ID = 19;
        player1.setId(player1ID);
        player1.setUsername("TestPlayer");

        testGame.setPlayer1(player1);
        testGame.setGameMode(GameMode.NORMAL);
        testGame.setIsPlaying(false);

        Game createdGame = gameService.createGame(testGame);

        //create user for player2
        User testUser1 = new User();
        testUser1.setUsername("testUsername22");
        testUser1.setPassword("test");
        testUser1.setBirthday("16.03.1994");
        User createdUser = userService.createUser(testUser1);

        System.out.println(createdUser.getId());

        Assert.assertNotNull(gameRepository.findByGameMode(GameMode.NORMAL));
        Assert.assertNotNull(createdGame.getPlayer1());
        Assert.assertNotNull(createdGame.getPlayer1().getWorker1());
        Assert.assertNotNull(createdGame.getPlayer1().getWorker2());

        Game joinedGame = gameService.joinLobby(userService.getUser(createdUser.getId()).getId(), createdGame.getId()).getBody();
        Assert.assertNotNull(gameRepository.findByGameMode(GameMode.NORMAL));
        Assert.assertNotNull(joinedGame.getPlayer1());
        Assert.assertNotNull(joinedGame.getPlayer1().getWorker1());
        Assert.assertNotNull(joinedGame.getPlayer1().getWorker2());
        Assert.assertNotNull(joinedGame.getPlayer2().getWorker1());
        Assert.assertNotNull(joinedGame.getPlayer2().getWorker2());
        Assert.assertEquals(joinedGame.getSize(), 2);

        gameRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test(expected = FullLobbyException.class)
    public void joinLobbyErr(){
        Assert.assertNull(gameRepository.getById(1));
        Game testGame = new Game();
        Player player1 = new Player();
        long player1ID = 29;
        player1.setId(player1ID);
        player1.setUsername("TestPlayer29");

        testGame.setPlayer1(player1);
        testGame.setGameMode(GameMode.NORMAL);
        testGame.setIsPlaying(false);

        Game createdGame = gameService.createGame(testGame);

        //create user for player2
        User testUser1 = new User();
        testUser1.setUsername("testUsername22");
        testUser1.setPassword("test");
        testUser1.setBirthday("16.03.1994");
        User createdUser = userService.createUser(testUser1);

        User testUser2 = new User();
        testUser2.setUsername("testUsername23");
        testUser2.setPassword("test");
        testUser2.setBirthday("16.03.1997");
        User createdUser2 = userService.createUser(testUser2);

        gameService.joinLobby(userService.getUser(createdUser.getId()).getId(), createdGame.getId());
        //try to join the full lobby
        gameService.joinLobby(userService.getUser(createdUser2.getId()).getId(), createdGame.getId());

        gameRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void leaveLobby(){
        Assert.assertNull(gameRepository.getById(1));
        Game testGame = new Game();
        Player player1 = new Player();
        long player1ID = 10;
        player1.setId(player1ID);
        player1.setUsername("TestPlayer10");

        testGame.setPlayer1(player1);
        testGame.setGameMode(GameMode.NORMAL);
        testGame.setIsPlaying(false);

        Game createdGame = gameService.createGame(testGame);

        Assert.assertNotNull(gameRepository.findByGameMode(GameMode.NORMAL));
        Assert.assertNotNull(createdGame.getPlayer1());
        Assert.assertNotNull(createdGame.getPlayer1().getWorker1());
        Assert.assertNotNull(createdGame.getPlayer1().getWorker2());

        gameService.leaveLobby(createdGame.getId(), player1.getId());
        Assert.assertEquals(gameService.getGames(), new ArrayList<>());
        Assert.assertNull(createdGame.getPlayer2());
        Assert.assertNotNull(createdGame.getPlayer1().getWorker1());
        Assert.assertNotNull(createdGame.getPlayer1().getWorker2());
        gameRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();
    }

    @Test(expected = NullPointerException.class)
    public void leaveLobbyErr(){
        Assert.assertNull(gameRepository.getById(1));
        Game testGame = new Game();
        Player player1 = new Player();
        long player1ID = 8;
        player1.setId(player1ID);
        player1.setUsername("TestPlayer8");

        testGame.setPlayer1(player1);
        testGame.setGameMode(GameMode.NORMAL);
        testGame.setIsPlaying(false);

        Game createdGame = gameService.createGame(testGame);
        //check if its possible to leave game with another id than the created one
        gameService.leaveLobby(createdGame.getId()+100, player1.getId());
        gameRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();
    }

    @Test
    public void setColor(){
        Assert.assertNull(gameRepository.getById(1));
        Game testGame = new Game();
        Player player1 = new Player();
        long player1ID = 25;
        player1.setId(player1ID);
        player1.setUsername("TestPlayer25");

        Player player2 = new Player();
        long player2Id = 26;
        player2.setId(player2Id);
        player2.setUsername("TestPlayer26");

        testGame.setPlayer1(player1);
        testGame.setGameMode(GameMode.NORMAL);
        testGame.setIsPlaying(false);
        testGame.setPlayer2(player2);

        Game createdGame = gameService.createGame(testGame);

        player1.setColor(Color.YELLOW);
        Assert.assertEquals(player1.getColor(),Color.YELLOW);
        player1.setColor(Color.BLUE);
        Assert.assertEquals(player1.getColor(),Color.BLUE);
        player2.setColor(Color.YELLOW);
        Assert.assertEquals(player2.getColor(),Color.YELLOW);

        gameRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();
    }

    @Test(expected = DuplicateException.class)
    public void setColorErr(){
        Assert.assertNull(gameRepository.getById(1));
        Game testGame = new Game();
        Player player1 = new Player();
        long player1ID = 26;
        player1.setId(player1ID);
        player1.setUsername("TestPlayer26");

        Player player2 = new Player();
        long player2Id = 30;
        player2.setId(player2Id);
        player2.setUsername("TestPlayer30");

        testGame.setPlayer1(player1);
        testGame.setGameMode(GameMode.NORMAL);
        testGame.setIsPlaying(false);

        Game createdGame = gameService.createGame(testGame);
        createdGame.setPlayer2(player2);
        //duplicate error since both players pick same color
        gameService.setColor(createdGame.getId(),player1.getId(), Color.YELLOW);
        gameService.setColor(createdGame.getId(),player2.getId(),Color.YELLOW);

        gameRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();
    }

    @Test
    public void setBeginner(){
        gameRepository.deleteAll();
        Assert.assertNull(gameRepository.getById(1));
        Game testGame = new Game();
        Player player1 = new Player();
        long player1ID = 27;
        player1.setId(player1ID);
        player1.setUsername("TestPlayer27");

        Player player2 = new Player();
        long player2Id = 31;
        player2.setId(player2Id);
        player2.setUsername("TestPlayer31");

        testGame.setPlayer1(player1);
        testGame.setGameMode(GameMode.NORMAL);
        testGame.setIsPlaying(false);

        Game createdGame = gameService.createGame(testGame);
        createdGame.setPlayer2(player2);
        gameRepository.save(createdGame);
        gameService.setBeginner(createdGame.getId());
        Assert.assertNotNull(player1);
        Assert.assertNotNull(player2);
        Assert.assertNotNull(createdGame);
        gameRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();
    }
    //NonExistingGameException because fake gameID is given and the check for player1 ID in setBeginner function will throw error
    @Test(expected = NonExistentGameException.class)
    public void setBeginnerErr(){
        gameRepository.deleteAll();
        Assert.assertNull(gameRepository.getById(1));
        Game testGame = new Game();
        Player player1 = new Player();
        long player1ID = 28;
        player1.setId(player1ID);
        player1.setUsername("TestPlayer28");
        Player player2 = new Player();
        long player2Id = 32;
        player2.setId(player2Id);
        player2.setUsername("TestPlayer32");

        testGame.setPlayer1(player1);
        testGame.setGameMode(GameMode.NORMAL);
        testGame.setIsPlaying(false);

        Game createdGame = gameService.createGame(testGame);
        createdGame.setPlayer2(player2);
        gameRepository.save(createdGame);
        long testGameId = 10;
        gameService.setBeginner(testGameId);
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();
    }

    @Test
    public void deleteGame(){
        gameRepository.deleteAll();
        Assert.assertNull(gameRepository.getById(1));
        Game testGame = new Game();
        Player player1 = new Player();
        long player1ID = 29;
        player1.setId(player1ID);
        player1.setUsername("TestPlayer29");
        Player player2 = new Player();
        long player2Id = 33;
        player2.setId(player2Id);
        player2.setUsername("TestPlayer33");

        testGame.setPlayer1(player1);
        testGame.setGameMode(GameMode.NORMAL);
        testGame.setIsPlaying(false);

        Game createdGame = gameService.createGame(testGame);
        createdGame.setPlayer2(player2);
        gameRepository.save(createdGame);
        Assert.assertNotNull(createdGame);

        gameService.deleteGame(createdGame.getId());

        Assert.assertNull(gameRepository.getById(createdGame.getId()));

        gameRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();
    }

    @Test(expected = NonExistentGameException.class)
    public void deleteGameErr(){
        gameRepository.deleteAll();
        Assert.assertNull(gameRepository.getById(1));
        Game testGame = new Game();
        Player player1 = new Player();
        long player1ID = 35;
        player1.setId(player1ID);
        player1.setUsername("TestPlayer35");
        Player player2 = new Player();
        long player2Id = 36;
        player2.setId(player2Id);
        player2.setUsername("TestPlayer36");

        testGame.setPlayer1(player1);
        testGame.setGameMode(GameMode.NORMAL);
        testGame.setIsPlaying(false);

        Game createdGame = gameService.createGame(testGame);
        createdGame.setPlayer2(player2);
        gameRepository.save(createdGame);

        gameService.deleteGame(createdGame.getId());

        //call deleteGame a second time --> should throw Exception
        gameService.deleteGame(createdGame.getId());

        gameRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();
    }

    @Test
    public void assignGodCard(){
        Assert.assertNull(gameRepository.getById(1));
        Game testGame = new Game();
        Player player1 = new Player();
        long player1ID = 45;
        player1.setId(player1ID);
        player1.setUsername("TestPlayer45");
        testGame.setPlayer1(player1);
        testGame.setGameMode(GameMode.NORMAL);
        testGame.setIsPlaying(false);

        Game createdGame = gameService.createGame(testGame);

        gameService.assignGodCard("Pan", player1.getId());
        Assert.assertEquals(workerNormalRepository.findById(player1.getWorker1().getWorkerId()).getGodCard(), GodCards.Pan);
        Assert.assertEquals(player1.getWorker1().getPosition(), -1);
        Assert.assertEquals(player1.getWorker2().getPosition(), -1);

        gameRepository.deleteAll();
        playerRepository.deleteAll();
        workerNormalRepository.deleteAll();
        userRepository.deleteAll();
    }



}