package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Main;

import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.entity.AppUser;
import ch.uzh.ifi.seal.soprafs19.entity.Board;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.Player;
import ch.uzh.ifi.seal.soprafs19.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes= Main.class)
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
    private GameService gameService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BoardService boardService;

    @Autowired
    private MockMvc mvc;

    public Game setUpTestNormalGame(){
        userRepository.deleteAll();
        gameRepository.deleteAll();
        //create 2 Users

        //User1
        AppUser testAppUser1 = new AppUser();
        testAppUser1.setUsername("testUsername1");
        testAppUser1.setPassword("test");
        testAppUser1.setBirthday("16.03.1994");

        AppUser createdAppUser1 = userService.createUser(testAppUser1);
        AppUser onlineAppUser1 = userService.checkUser(createdAppUser1);


        //User2
        AppUser testAppUser3 = new AppUser();
        testAppUser3.setUsername("testUsername3");
        testAppUser3.setPassword("test");
        testAppUser3.setBirthday("16.03.1994");

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
        testAppUser1.setBirthday("16.03.1994");

        AppUser createdAppUser1 = userService.createUser(testAppUser1);
        AppUser onlineAppUser1 = userService.checkUser(createdAppUser1);


        //User2
        AppUser testAppUser3 = new AppUser();
        testAppUser3.setUsername("testUsername3");
        testAppUser3.setPassword("test");
        testAppUser3.setBirthday("16.03.1994");

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
        boardRepository.save(tempBoard);

        return tempGame1;
    }
    @Test
    public void newBoard() throws Exception {

        //  create game to save into repository
        Game newGame = new Game();
        Player player1 = new Player();
        player1.setId(1L);
        newGame.setPlayer1(player1);
        newGame.setGameMode(GameMode.NORMAL);
        GameService gameService = new GameService(gameRepository,  playerService,  workerNormalRepository,  playerRepository,  boardRepository);
        gameService.createGame(newGame);

        // create newBoard to be returned
        Board testBoard = new Board();
        testBoard.setId(newGame.getId());
        boardRepository.save(testBoard);

        // MVC test
        MvcResult result =  this.mvc.perform(get("/games/{gameId}/board/create", newGame.getId()))
                .andExpect(status().isOk())
                .andReturn();

        // check if the returned result is the same as that in boardrepository
        String content =  result.getResponse().getContentAsString();
        System.out.println(content);
        ObjectMapper mapper = new ObjectMapper();
        Board newBoard = mapper.readValue(content, Board.class);
        assert(newBoard.equals(testBoard));

        userRepository.deleteAll();
        playerRepository.deleteAll();
    }
    //@PutMapping("board/{boardId}")
    @Test
    public void getBoard() throws Exception{
        userRepository.deleteAll();
        playerRepository.deleteAll();
        gameRepository.deleteAll();
        boardRepository.deleteAll();
        Game currentGame = setUpTestNormalGame();
        Board currentBoard = boardService.getBoard(currentGame.getId());

        this.mvc.perform(get("board/{boardId}", 1))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
