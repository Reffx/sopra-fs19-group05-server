package ch.uzh.ifi.seal.soprafs19.repository;

import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("gameRepository")
public interface GameRepository extends CrudRepository<Game, Long> {
    public Game getById(long id);
    public Iterable<Game> findByGameMode(GameMode gameMode);      //  for lobby in the frontend
}
