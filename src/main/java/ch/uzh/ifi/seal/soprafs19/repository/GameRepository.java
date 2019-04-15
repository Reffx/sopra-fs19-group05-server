package ch.uzh.ifi.seal.soprafs19.repository;

import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.Player;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Long> {
    public Game findById(long id);
    public Game findByPlayer1orPlayer2(Long playerId);
    public Iterable<Game> findByGameMode(GameMode gameMode);      //  for lobby in the frontend
}
