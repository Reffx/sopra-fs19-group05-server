package ch.uzh.ifi.seal.soprafs19.repository;

import ch.uzh.ifi.seal.soprafs19.entity.Playfield;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository("playfieldRepository")
public interface PlayfieldRepository extends CrudRepository<Playfield, Long> {
    Playfield findById(long playFieldId);
}
