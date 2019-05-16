package ch.uzh.ifi.seal.soprafs19.repository;

import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.entity.Record;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository("recordRepository")
public interface RecordRepository extends CrudRepository<Record, Long> {
    Record getById(long Id);
    Iterable<Record> findByGameMode(GameMode gameMode);

//    @Query("select r from recordRepository r where r.gameMode = ?0 and r.isDone = ?1")
//    Iterable<Record> findByGameModeAndIsDone(GameMode gameMode, boolean isDone);
}
