package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.entity.Player;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        System.out.println(gameRepository.getById(gameId));
        return new ResponseEntity<Game>(gameRepository.getById(gameId), HttpStatus.FOUND);
    }

    //  create a game
    public ResponseEntity<Game> createGame(Game game) {

        Player player1 = game.getPlayer1();   //  set the player1 as the creator
        //  save the player1 to the playerRepository
        playerService.createPlayer(player1);

        game.setPlayer1(player1);
        gameRepository.save(game);
        return new ResponseEntity<Game>(game, HttpStatus.CREATED); //   response code:201, frontend fetch the gameId in the body
    }

    //  update a game, add player
    public ResponseEntity<String> addPlayer2(Long userId, Long gameId) {
        Game game = gameRepository.getById(gameId);

        //  create new player
        Player player2 = new Player();
        player2.setId(userId);
        //  save the player1 to the playerRepository
        playerService.createPlayer(player2);

        game.setPlayer2(player2);
        gameRepository.save(game);
        return new ResponseEntity<String>(HttpStatus.OK);   // response code 200
    }

    // public ResponseEntity<String> addPlayer2(User user, Long gameId){

        //DA: I would give the function the whole user (imported the entity and assign ID & username via createPlayer

       // Game game = gameRepository.getById(gameId);

        //Player player2 = new Player();
        //playerService.createPlayer(player2);

        //game.setPlayer2(player2);
        //gameRepository.save(game);
        //return new ResponseEntity<String>(HttpStatus.OK);
   // }

    //  TO DO: update a game, remove player ect.
    public ResponseEntity<String> removePlayer(Long userId, Long gameId) {
        Game game = gameRepository.getById(gameId);

        //  to find the player in the database and remove it, cascade deletion of player2 in the Game instance

        gameRepository.save(game);
        return new ResponseEntity<String>(HttpStatus.OK);   // response code 200
    }

    //  TO DO: delete a game. when finished or exit
    public ResponseEntity<String> deleteGame(Long userId) {
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT); // response code: 204
    }
}
