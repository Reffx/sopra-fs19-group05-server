package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Main;

import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.entity.AppUser;
import ch.uzh.ifi.seal.soprafs19.entity.Board;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.Player;
import ch.uzh.ifi.seal.soprafs19.repository.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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
public class WorkerControllerTest {

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
    private UserService userService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private WorkerService workerService;

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
        playerRepository.save(playerOne);
        playerRepository.save(playerTwo);
        boardRepository.save(tempBoard);
        gameRepository.save(tempGame1);
        return tempGame1;
    }
    @Test
    public void place() throws Exception{
        userRepository.deleteAll();
        playerRepository.deleteAll();
        gameRepository.deleteAll();
        boardRepository.deleteAll();
        Game currentGame = setUpTestNormalGame();
        Game testGame= gameRepository.getById(currentGame.getId());
        // MVC test
        mvc.perform(put("/games/{gameId}/{fieldNum}/{workerId}/place", testGame.getId(),3, testGame.getPlayer1().getWorker1().getWorkerId()))
                .andExpect(status().isOk())
                .andReturn();

        mvc.perform(put("/games/{gameId}/{fieldNum}/{workerId}/move", testGame.getId(),4, testGame.getPlayer1().getWorker1().getWorkerId()))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void moveTo() throws Exception{
        userRepository.deleteAll();
        playerRepository.deleteAll();
        gameRepository.deleteAll();
        boardRepository.deleteAll();
        Game currentGame = setUpTestNormalGame();
        Game testGame= gameRepository.getById(currentGame.getId());
        // MVC test
        this.mvc.perform(put("/games/{gameId}/{fieldNum}/{workerId}/place", testGame.getId(),3, testGame.getPlayer1().getWorker1().getWorkerId()))
                .andExpect(status().isOk())
                .andReturn();

        this.mvc.perform(put("/games/{gameId}/{fieldNum}/{workerId}/move", testGame.getId(),4, testGame.getPlayer1().getWorker1().getWorkerId()))
                .andExpect(status().is(200)).andDo(print());


    }


    @Test
    public void highlightFieldBuild() throws Exception{
        userRepository.deleteAll();
        playerRepository.deleteAll();
        gameRepository.deleteAll();
        boardRepository.deleteAll();
        Game currentGame = setUpTestNormalGame();
        Game testGame= gameRepository.getById(currentGame.getId());
        workerService.placeWorker(testGame.getId(),testGame.getPlayer1().getWorker1().getWorkerId(),3);
        workerService.moveTo(testGame.getId(),testGame.getPlayer1().getWorker1().getWorkerId(),3);
        this.mvc.perform(get("/games/{gameId}/{fieldNum}/highlight/build", testGame.getId(),3))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void highlightFieldMove() throws Exception{
        userRepository.deleteAll();
        playerRepository.deleteAll();
        gameRepository.deleteAll();
        boardRepository.deleteAll();
        Game currentGame = setUpTestNormalGame();
        Game testGame= gameRepository.getById(currentGame.getId());

        workerService.placeWorker(testGame.getId(),testGame.getPlayer1().getWorker1().getWorkerId(),3);
        this.mvc.perform(get("/games/{gameId}/{fieldNum}/highlight/move", testGame.getId(),3))
                .andExpect(status().isOk())
                .andReturn();

    }

}
