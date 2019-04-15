package ch.uzh.ifi.seal.soprafs19.repository;

import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("gameRepository")
public interface GameRepository extends CrudRepository<Game, Long> {
    Game findByGameMode(String gameMode);
    Game findByGameId(Long gameId);
}
