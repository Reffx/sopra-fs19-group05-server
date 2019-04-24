package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.Color;
import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.Player;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerService playerService;

    @Autowired
    public GameService(GameRepository gameRepository, PlayerService playerService) {
        this.gameRepository = gameRepository;
        this.playerService = playerService;
    }

    //  find all the games
    public Iterable<Game> getGames() {
        return this.gameRepository.findAll();
    }

    //  find all the NORMAL/GOD mode game
    public ResponseEntity<Iterable<Game>> getModeGames(GameMode gameMode) {
        return new ResponseEntity<Iterable<Game>>(gameRepository.findByGameMode(gameMode), HttpStatus.FOUND);
    }

    //  find a game by id
    public ResponseEntity<Game> getGame(long gameId) {
        return new ResponseEntity<Game>(gameRepository.getById(gameId), HttpStatus.FOUND);
    }

    //  create a game
    public ResponseEntity<Game> createGame(Game game) {

        Player player1 = game.getPlayer1();   //  set the player1 as the creator
        //  save the player1 to the playerRepository
        player1.setColor(Color.BLUE);

//        playerService.createPlayer(player1);
//        playerService.savePlayer(player1);

        //  save first to get gameId
        game.setPlayer1(player1);
        gameRepository.save(game);

        //  set player gameId
        long gameId = game.getId();
        System.out.println(gameId);
        player1.setGameId(gameId);

        //  save again to save the gameId in Player
        gameRepository.save(game);

        return new ResponseEntity<Game>(game, HttpStatus.CREATED); //   response code:201, frontend fetch the gameId in the body
    }

    //  update a game, add player
    public ResponseEntity<String> addPlayer2(Long userId, Long gameId) {
        Game game = gameRepository.getById(gameId);

        //  create new player
        Player player2 = new Player();
        player2.setId(userId);
        player2.setGameId(gameId);
        //  save the player1 to the playerRepository

//        playerService.createPlayer(player2);
//        playerService.savePlayer(player2);

        game.setPlayer2(player2);
        game.setSize(2);
        gameRepository.save(game);
        return new ResponseEntity<String>(HttpStatus.OK);   // response code 200
    }

    //  update a game, remove player2.
    public ResponseEntity<String> removePlayer(Long gameId) {
        Game game = gameRepository.getById(gameId);

        //  to find the player in the database and remove it, cascade deletion of player2 in the Game instance
        game.setPlayer2(null);
        gameRepository.save(game);
        return new ResponseEntity<String>(HttpStatus.OK);   // response code 200
    }

    //  set player color
    public ResponseEntity<String> setColor(Long gameId, Long playerId, Color color) {
        //  check if there's color conflict
        Game game = gameRepository.getById(gameId);
        Player playerX = playerService.getPlayer(playerId);

        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();

        if (player1 == playerX) {
            if (player2.getColor() == color) {
                return new ResponseEntity<String>(HttpStatus.CONFLICT);
            } else {
                player1.setColor(color);
                playerService.savePlayer(player1);
                return new ResponseEntity<String>(color.toString(), HttpStatus.OK);
            }
        } else if (player2 == playerX) {
            if (player1.getColor() == color) {
                return new ResponseEntity<String>(HttpStatus.CONFLICT);
            } else {
                player2.setColor(color);
                playerService.savePlayer(player1);
                return new ResponseEntity<String>(color.toString(), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
    }


    //  set player2 status
    public ResponseEntity<String> setStatus(Long gameId, Long playerId) {
        Player player = playerService.getPlayer(playerId);
        boolean status = player.getStatus();
        player.setStatus(!status);  //  negation of the original status

        playerService.savePlayer(player);
        return new ResponseEntity<String>(HttpStatus.OK);
    }
    
    //  set beginner
    public ResponseEntity<Long> setBeginner(Long gameId) {
        //  get playerId
        Long player1Id = gameRepository.getById(gameId).getPlayer1().getId();
        Long player2Id = gameRepository.getById(gameId).getPlayer2().getId();

        //  randomly select a player
        Random random = new Random();

        int id_rand = random.nextInt(2);
        if (id_rand == 0) {
            return new ResponseEntity<Long>(player1Id, HttpStatus.OK);
        }
        return new ResponseEntity<Long>(player2Id, HttpStatus.OK);
    }


    //  delete a game. when player1 exit
    public ResponseEntity<String> deleteGame(Long gameId) {
        gameRepository.deleteById(gameId);
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT); // response code: 204
    }
}
