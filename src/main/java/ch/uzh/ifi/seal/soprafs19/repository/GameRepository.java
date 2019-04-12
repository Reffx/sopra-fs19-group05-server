package ch.uzh.ifi.seal.soprafs19.repository;

import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.Player;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Long> {
    public Game findById(long id);
    public Game findByaListContaining(Player player); // using player object to find the game
}
