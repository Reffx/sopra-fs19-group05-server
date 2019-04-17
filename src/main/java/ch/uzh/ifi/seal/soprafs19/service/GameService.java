package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.Player;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GameService {
    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
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
        Long userId = game.getPlayer1();            //  set the player1 as the creator

        // TO DO:  create a player or delay it to the ready button
        gameRepository.save(game);
        return new ResponseEntity<Game>(game, HttpStatus.CREATED); //   response code:201, frontend fetch the gameId in the body
    }

    //  TO DO: update a game, add player ect.
    public ResponseEntity<String> updateGame(Long userId, Long gameId) {
        Game game = gameRepository.getById(gameId);
        //  test
        System.out.println(gameId);

        game.setPlayer2(userId);
        gameRepository.save(game);
        return new ResponseEntity<String>(HttpStatus.OK);   // response code 200
    }

    //  TO DO: delete a game. when finished or exit
    public ResponseEntity<String> deleteGame(Long userId) {
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT); // response code: 204
    }
}
