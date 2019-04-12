package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.Player;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void createGame(int userId) {

    }

    //  TO DO: update a game, add player ect.


    //  TO DO: delete a game. when finished or exit


}
