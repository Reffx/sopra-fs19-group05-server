package ch.uzh.ifi.seal.soprafs19.service;


import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.entity.Board;
import ch.uzh.ifi.seal.soprafs19.entity.Record;
import ch.uzh.ifi.seal.soprafs19.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RecordService {
    private final RecordRepository recordRepository;

    @Autowired
    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    //  find all records
    public Iterable<Record> allRecords() {
        return recordRepository.findAll();
    }

    //  find records by gameMode
    public Iterable<Record> findByGameMode(GameMode gameMode) {
        return recordRepository.findByGameMode();
    }

    // add a state(Board) to the list in Record
    public void addState(Long gameId, Board board) {
        Record record = recordRepository.getById(gameId);
        List<Board> states = record.getStates();
        states.add(board);
    }

}

