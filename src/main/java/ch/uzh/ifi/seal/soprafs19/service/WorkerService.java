package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.entity.*;

import ch.uzh.ifi.seal.soprafs19.repository.WorkerNormalRepository;
import ch.uzh.ifi.seal.soprafs19.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class WorkerService {
    private final PlayerService playerService;
    private final BoardService boardService;
    private final GameService gameService;
    private final WorkerNormalRepository workerNormalRepository;

    @Autowired
    public WorkerService(WorkerNormalRepository workerNormalRepository,PlayerService playerService, BoardService boardService, GameService gameService) {
        this.playerService = playerService;
        this.boardService = boardService;
        this.gameService = gameService;
        this.workerNormalRepository = workerNormalRepository;
    }


    //  convert coordinates to fieldNum
    private int coordsToId(int x, int y) {
        return (x * 5 + y);
    }

    public ResponseEntity<Integer> placeWorker(long gameId, int workerId, int dest){
        Board board = boardService.getBoard(gameId);
        WorkerNormal placingWorker = workerNormalRepository.findById(workerId);
        Field fieldToPlace = boardService.getField(placingWorker.getPosition(), gameId);

        fieldToPlace.setOccupier(placingWorker);
        placingWorker.setPosition(dest);

        workerNormalRepository.save(placingWorker);


        boardService.updateBoard(board);

        return new ResponseEntity<Integer>(dest, HttpStatus.OK);
    }

    public ResponseEntity<List<Integer>> highlightField(int fieldNum, long gameId) {
        Field currentField = boardService.getField(fieldNum, gameId);
        Board board = boardService.getBoard(gameId);
        List<Field>  allFields = board.getAllFields();
        //todo: check available moving options and send them back to frontend
        int x = currentField.getX_coordinate();
        int y = currentField.getY_coordinate();

        List<Integer> highlightedFields = new ArrayList<Integer>();

        int x1 = x - 1; int y1 = y - 1;
        int x2 = x - 1; int y2 = y;
        int x3 = x - 1; int y3 = y + 1;
        int x4 = x; int y4 = y - 1;
        int x5 = x; int y5 = y + 1;
        int x6 = x + 1; int y6 = y - 1;
        int x7 = x + 1; int y7 = y;
        int x8 = x + 1; int y8 = y + 1;

        if(x1 <= 4 && y1 <= 4 && x1 >= 0 && y1 >0) {
            Field highlight1 = boardService.getField(coordsToId(x1,y1), gameId);
            if(highlight1.getHeight() != 4 && highlight1.getOccupier() == null){
                highlightedFields.add(highlight1.getFieldNum());
            }
        }
        if(x2 <= 4 && y2 <= 4 && x2 >= 0 && y2 >0) {
            Field highlight2 = boardService.getField(coordsToId(x2, y2), gameId);
            if (highlight2.getHeight() != 4 && highlight2.getOccupier() == null) {
                highlightedFields.add(highlight2.getFieldNum());
            }
        }
        if(x3 <= 4 && y3 <= 4 && x3 >= 0 && y3 >0) {
            Field highlight3 = boardService.getField(coordsToId(x3, y3), gameId);
            if (highlight3.getHeight() != 4 && highlight3.getOccupier() == null) {
                highlightedFields.add(highlight3.getFieldNum());
            }
        }
        if(x4 <= 4 && y4 <= 4 && x4 >= 0 && y4 >0) {
            Field highlight4 = boardService.getField(coordsToId(x4, y4), gameId);
            if (highlight4.getHeight() != 4 && highlight4.getOccupier() == null) {
                highlightedFields.add(highlight4.getFieldNum());
            }
        }
        if(x5 <= 4 && y5 <= 4 && x5 >= 0 && y5 >0) {
            Field highlight5 = boardService.getField(coordsToId(x5, y5), gameId);
            if (highlight5.getHeight() != 4 && highlight5.getOccupier() == null) {
                highlightedFields.add(highlight5.getFieldNum());
            }
        }
        if(x6 <= 4 && y6 <= 4 && x6 >= 0 && y6 >0) {
            Field highlight6 = boardService.getField(coordsToId(x6, y6), gameId);
            if (highlight6.getHeight() != 4 && highlight6.getOccupier() == null) {
                highlightedFields.add(highlight6.getFieldNum());
            }
        }
        if(x7 <= 4 && y7 <= 4 && x7 >= 0 && y7 >0) {
            Field highlight7 = boardService.getField(coordsToId(x7, y7), gameId);
            if (highlight7.getHeight() != 4 && highlight7.getOccupier() == null) {
                highlightedFields.add(highlight7.getFieldNum());
            }
        }
        if(x8 <= 4 && y8 <= 4 && x8 >= 0 && y8 >0) {
            Field highlight8 = boardService.getField(coordsToId(x8, y8), gameId);
            if (highlight8.getHeight() != 4 && highlight8.getOccupier() == null) {
                highlightedFields.add(highlight8.getFieldNum());
            }
        }
        return new ResponseEntity<List<Integer>>(highlightedFields, HttpStatus.OK);
    }
    public ResponseEntity<Integer> moveTo(long gameId, int workerId, int dest){
        Board board = boardService.getBoard(gameId);
        WorkerNormal movingWorker = workerNormalRepository.findById(workerId);
        Field currentField = boardService.getField(movingWorker.getPosition(), gameId);
        Field destination = boardService.getField(dest, gameId);

        destination.setOccupier(movingWorker);
        movingWorker.setPosition(destination.getFieldNum());
        currentField.setOccupier(null);
        workerNormalRepository.save(movingWorker);

        // DA: if currentField.getHeight()=2 && destination.getHeight() = 3
        //         WinningCondition(workerId)

        boardService.updateBoard(board);

        return new ResponseEntity<Integer>(destination.getFieldNum(), HttpStatus.OK);
    }

    public ResponseEntity<String> build(long gameId, int fieldNum){
        Board board = boardService.getBoard(gameId);
        Field currentField = boardService.getField(fieldNum, gameId);

        int h = currentField.getHeight();
        currentField.setHeight(h + 1);

        boardService.updateBoard(board);

        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
