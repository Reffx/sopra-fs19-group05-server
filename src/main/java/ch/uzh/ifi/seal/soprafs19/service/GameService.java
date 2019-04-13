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
    public GameService(GameRepository gameRepository) { this.gameRepository = gameRepository; }

    //  find all the games
    public Iterable<Game> getGames() { return this.gameRepository.findAll();}

    //  TO DO: create a game
    public ResponseEntity<String> createGame(int userId, GameMode gameMode) {
        Game game = new Game();
        game.setGameMode(gameMode);

    }

    //  TO DO: update a game, add player ect.
    public ResponseEntity<String> updateGame(int userId) {

    }

    //  TO DO: delete a game. when finished or exit
    public ResponseEntity<String> deleteGame(int userId) {

    }

}
