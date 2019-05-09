package ch.uzh.ifi.seal.soprafs19.service;


import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.entity.Board;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.Record;
import ch.uzh.ifi.seal.soprafs19.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class RecordService {
    private final RecordRepository recordRepository;
    private final GameService gameService;

    @Autowired
    public RecordService(RecordRepository recordRepository, GameService gameService) {
        this.recordRepository = recordRepository;
        this.gameService = gameService;
    }

    //  find all records
    public Iterable<Record> allRecords() {
        return recordRepository.findAll();
    }

    //  find  record by Id
    public Record findById(long gameId) {
        return recordRepository.getById(gameId);
    }

    //  find records by gameMode
    public Iterable<Record> findByGameMode(GameMode gameMode) {
        return recordRepository.findByGameMode(gameMode);
    }

    //  randomly find record by gameMode
    public ResponseEntity<Record> findOne(GameMode gameMode) {
        Iterable<Record> records = recordRepository.findByGameMode(gameMode);

        //  find finished game records
        Iterable<Record> recordsDone = new ArrayList<>();
        for (Record r : records) {
            if (r.getIsDone()) {
                ((ArrayList<Record>) recordsDone).add(r);
            }
        }

        //  check if no finished record
        int size = ((ArrayList<?>) recordsDone).size(); // cast to collection and get its size
        if (size == 0) {
            return new ResponseEntity<Record>(HttpStatus.NOT_FOUND);
        }

        //  get a randon index and return one record
        Random rand = new Random();
        int idx = rand.nextInt(size);
        Record record = ((ArrayList<Record>) recordsDone).get(idx);
        return new ResponseEntity<Record> (record, HttpStatus.OK);
    }

    // add a state(Board) to the list in Record
    public void addState(Long gameId, Board board) {
        Record record = recordRepository.getById(gameId);

        //  if record doesn't exist, create a new record
        if (record == null) {
            record = createRecord(gameId);
        }

        List<Board> states = record.getStates();
        states.add(board);
        record.setStates(states);

        //  save the record
        recordRepository.save(record);
    }

    //  create a reocrd
    public Record createRecord(Long gameId) {
        Record record = new Record();
        record.setId(gameId);

        //  initialize states
        List<Board> states = new ArrayList<>();
        record.setStates(states);

        //  set gameMode
        Game game = gameService.getGame(gameId).getBody();
        GameMode gameMode = game.getGameMode();
        record.setGameMode(gameMode);

        return record;
    }



}

