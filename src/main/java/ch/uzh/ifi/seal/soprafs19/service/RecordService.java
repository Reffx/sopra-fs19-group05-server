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

    //  find records by gameMode
    public Iterable<Record> findByGameMode(GameMode gameMode) {
        return recordRepository.findByGameModeAndIsDone(gameMode, true);
    }

    //  randomly find record by gameMode
    public ResponseEntity<Record> findOne(GameMode gameMode) {
        Iterable<Record> records = recordRepository.findByGameModeAndIsDone(gameMode, true);
        int size = ((ArrayList<?>) records).size(); // cast to collection and get its size

        if (size == 0) {
            return new ResponseEntity<Record>(HttpStatus.NOT_FOUND);
        }

        //  get a randon index and return one record
        Random rand = new Random();
        int idx = rand.nextInt(size);
        Record record = ((ArrayList<Record>) records).get(idx);
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
        System.out.println("Record3" + record);
        System.out.println("board" + board);
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

