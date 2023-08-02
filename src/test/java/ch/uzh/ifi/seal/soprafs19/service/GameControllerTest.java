package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Main;

import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs19.constant.GodCards;
import ch.uzh.ifi.seal.soprafs19.entity.AppUser;
import ch.uzh.ifi.seal.soprafs19.entity.Board;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.Player;
import ch.uzh.ifi.seal.soprafs19.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
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
import org.springframework.transaction.annotation.Transactional;


import static org.hamcrest.core.IsEqual.equalTo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;



@RunWith(SpringRunner.class)
@SpringBootTest(classes= Main.class)
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
    @Autowired
    private GameService gameService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private UserService userService;

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


    @Test
    public void getGame() throws Exception {

        //  create game to save into repository
        Game newGame = new Game();
        newGame.setGameMode(GameMode.NORMAL);
        Player player1 = new Player();
        player1.setId(1L);
        newGame.setPlayer1(player1);
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


    @Test
    public void getModeGames() throws Exception {
        Game currentGame = setUpTestNormalGame();

        // MVC test
        this.mvc.perform(get("/games/mode/{gameMode}", GameMode.NORMAL))
                .andExpect(status().isFound())
                .andReturn();
        Assert.assertEquals(currentGame.getGameStatus(), GameStatus.Start);
        Assert.assertEquals(currentGame.getGameMode(), GameMode.NORMAL);

    }

    @Test
    public void leaveLobby() throws Exception {

        //  create game to save into repository
        Game newGame = new Game();
        newGame.setGameMode(GameMode.NORMAL);
        //  create new Players
        Player player1 = new Player();
        player1.setId(1L);
        Player player2 = new Player();
        player2.setId(2L);

        newGame.setPlayer1(player1);
        newGame.setPlayer2(player2);
        gameService.createGame(newGame);
        Long id = newGame.getId();
        Assert.assertNotNull(gameRepository.getById(id));

        // MVC test
        this.mvc.perform(put("/games/{gameId}/{playerId}", id, 2L))
                .andExpect(status().isOk());

//        this.mvc.perform(put("/games/{gameId}/{playerId}", id, 1L))
//                .andExpect(status().isNotFound());
//
//        //  assert the game in the repository is deleted after two players leave the lobby
//        Assert.assertNull(gameRepository.getById(id));
    }


    @Test
    public void setStatus() throws Exception {

        //  create game to save into repository
        Game newGame = new Game();
        newGame.setGameMode(GameMode.NORMAL);
        //  create new Players
        Player player1 = new Player();
        player1.setId(1L);
        Player player2 = new Player();
        player2.setId(2L);

        newGame.setPlayer1(player1);
        newGame.setPlayer2(player2);
        gameService.createGame(newGame);
        Long id = newGame.getId();
        Assert.assertEquals(false, player1.getStatus());

        // MVC test
        this.mvc.perform(put("/games/{gameId}/{playerId}/status", id, 1L))
                .andExpect(status().isOk());


        //  assert the game in the repository is deleted after two players leave the lobby
        Assert.assertEquals(true, newGame.getPlayer1().getStatus());
    }

    @Test
    public void setBeginner() throws Exception {

        //  create game to save into repository
        Game newGame = new Game();
        newGame.setGameMode(GameMode.NORMAL);
        //  create new Players
        Player player1 = new Player();
        player1.setId(1L);
        Player player2 = new Player();
        player2.setId(2L);

        newGame.setPlayer1(player1);
        newGame.setPlayer2(player2);
        gameService.createGame(newGame);
        Long id = newGame.getId();

        // MVC test
        MvcResult result =  this.mvc.perform(get("/games/{gameId}/beginner", id))
                .andExpect(status().isOk())
                .andReturn();

        String content =  result.getResponse().getContentAsString();
        long testId = Long.parseLong(content);
        //  assert the game in the repository is deleted after two players leave the lobby
        assert(testId == player1.getId() || testId == player2.getId());
    }

    public Game setUpTestNormalGame(){
        userRepository.deleteAll();
        gameRepository.deleteAll();
        //create 2 Users

        //User1
        AppUser testAppUser1 = new AppUser();
        testAppUser1.setUsername("testUsername1");
        testAppUser1.setPassword("test");

        AppUser createdAppUser1 = userService.createUser(testAppUser1);
        AppUser onlineAppUser1 = userService.checkUser(createdAppUser1);


        //User2
        AppUser testAppUser3 = new AppUser();
        testAppUser3.setUsername("testUsername3");
        testAppUser3.setPassword("test");

        AppUser createdAppUser3 = userService.createUser(testAppUser3);

        AppUser onlineAppUser3 = userService.checkUser(createdAppUser3);

        //create a Game with User 1

        Game testGame1 = new Game();
        Player playerOne = new Player();
        testGame1.setPlayer1(playerOne);
        testGame1.setGameMode(GameMode.NORMAL);
        testGame1.setIsPlaying(false);

        playerOne.setId(createdAppUser1.getId());
        playerOne.setUsername(createdAppUser1.getUsername());
        Game createdGame1 = gameService.createGame(testGame1);


        //Join the created game with createdUser2 (Player2)
        Game tempGame1 = gameService.joinLobby(createdAppUser3.getId(), createdGame1.getId()).getBody();

        Player playerTwo = tempGame1.getPlayer2();

        Board tempBoard = boardService.getBoard(tempGame1.getId());

        return tempGame1;
    }

    public Game setUpTestGodGame(){
        userRepository.deleteAll();
        gameRepository.deleteAll();


        //create 2 Users

        //User1
        AppUser testAppUser1 = new AppUser();
        testAppUser1.setUsername("testUsername1");
        testAppUser1.setPassword("test");

        AppUser createdAppUser1 = userService.createUser(testAppUser1);
        AppUser onlineAppUser1 = userService.checkUser(createdAppUser1);


        //User2
        AppUser testAppUser3 = new AppUser();
        testAppUser3.setUsername("testUsername3");
        testAppUser3.setPassword("test");

        AppUser createdAppUser3 = userService.createUser(testAppUser3);

        AppUser onlineAppUser3 = userService.checkUser(createdAppUser3);

        //create a Game with User 1

        Game testGame1 = new Game();
        Player playerOne = new Player();
        testGame1.setPlayer1(playerOne);
        testGame1.setGameMode(GameMode.GOD);
        testGame1.setIsPlaying(false);

        playerOne.setId(createdAppUser1.getId());
        playerOne.setUsername(createdAppUser1.getUsername());
        Game createdGame1 = gameService.createGame(testGame1);


        //Join the created game with createdUser2 (Player2)
        Game tempGame1 = gameService.joinLobby(createdAppUser3.getId(), createdGame1.getId()).getBody();

        Player playerTwo = tempGame1.getPlayer2();

        Board tempBoard = boardService.getBoard(tempGame1.getId());

        return tempGame1;
    }

    @Test
    public void joinLobby() throws Exception{
        userRepository.deleteAll();
        playerRepository.deleteAll();
        gameRepository.deleteAll();
        boardRepository.deleteAll();


        //create 2 Users

        //User1
        AppUser testAppUser1 = new AppUser();
        testAppUser1.setUsername("testUsername1");
        testAppUser1.setPassword("test");

        AppUser createdAppUser1 = userService.createUser(testAppUser1);
        AppUser onlineAppUser1 = userService.checkUser(createdAppUser1);


        //User2
        AppUser testAppUser3 = new AppUser();
        testAppUser3.setUsername("testUsername3");
        testAppUser3.setPassword("test");

        AppUser createdAppUser3 = userService.createUser(testAppUser3);

        AppUser onlineAppUser3 = userService.checkUser(createdAppUser3);

        //create a Game with User 1

        Game testGame1 = new Game();
        Player playerOne = new Player();
        testGame1.setPlayer1(playerOne);
        testGame1.setGameMode(GameMode.GOD);
        testGame1.setIsPlaying(false);

        playerOne.setId(createdAppUser1.getId());
        playerOne.setUsername(createdAppUser1.getUsername());
        Game createdGame1 = gameService.createGame(testGame1);


        //Join the created game with createdUser2 (Player2)
        Game tempGame1 = gameService.joinLobby(createdAppUser3.getId(), createdGame1.getId()).getBody();

        Player playerTwo = tempGame1.getPlayer2();

        // MVC test
        this.mvc.perform(put("/games/{gameId}/{playerId}", tempGame1.getId(), playerTwo.getId()))
                .andExpect(status().isOk());

        this.mvc.perform(put("/games/{gameId}/player", tempGame1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerTwo.getId().toString()))
                .andExpect(status().is(200)).andDo(print());

    }


    @Test
    public void surrender() throws Exception{
        userRepository.deleteAll();
        playerRepository.deleteAll();
        gameRepository.deleteAll();
        boardRepository.deleteAll();
        Game currentGame = setUpTestNormalGame();
        this.mvc.perform(put("/games/{gameId}/{playerId}/surrender", currentGame.getId(), currentGame.getPlayer1().getId()))
                .andExpect(status().isOk());

        Assert.assertNotEquals(currentGame.getGameStatus(), GameStatus.Start);
    }

    //@PutMapping("/games/{gameId}/{playerId}/GodCard")
    @Test
    public void assignGodCard() throws Exception{
        userRepository.deleteAll();
        playerRepository.deleteAll();
        gameRepository.deleteAll();
        boardRepository.deleteAll();

        Game currentGame = setUpTestNormalGame();

        this.mvc.perform(put("/games/{gameId}/{playerId}/GodCard", currentGame.getId(), currentGame.getPlayer1().getId())
                .content("Artemis"))
                .andExpect(status().isOk());
        Assert.assertEquals(currentGame.getPlayer1().getWorker1().getGodCard(), GodCards.Artemis);
        this.mvc.perform(put("/games/{gameId}/{playerId}/GodCard", currentGame.getId(), currentGame.getPlayer1().getId())
                .content("InactiveArtemis"))
                .andExpect(status().isOk());
        Assert.assertEquals(currentGame.getPlayer1().getWorker1().getGodCard(), GodCards.InactiveArtemis);
        Assert.assertEquals(currentGame.getPlayer1().getWorker2().getGodCard(), GodCards.InactiveArtemis);

    }
}





