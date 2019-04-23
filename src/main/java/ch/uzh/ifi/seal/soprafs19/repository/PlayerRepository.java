package ch.uzh.ifi.seal.soprafs19.repository;

import ch.uzh.ifi.seal.soprafs19.entity.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("playerRepository")
public interface PlayerRepository extends CrudRepository<Player, Long> {
<<<<<<< HEAD
	Player findByUserId(Long userId);
//	Player findByGameId(Long gameId);
}
=======
	Player getById(Long userId);
	Player getByGameId(Long gameId);
}

>>>>>>> feature_Game_backend
