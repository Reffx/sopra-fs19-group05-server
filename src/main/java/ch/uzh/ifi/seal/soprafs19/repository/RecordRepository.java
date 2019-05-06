package ch.uzh.ifi.seal.soprafs19.repository;

import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.entity.Record;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository("recordRepository")
public interface RecordRepository extends CrudRepository<Record, Long> {
    Record getById(long Id);
    Iterable<Record> findByGameMode(GameMode gameMode);
}
