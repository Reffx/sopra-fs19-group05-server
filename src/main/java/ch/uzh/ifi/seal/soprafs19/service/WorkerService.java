package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.entity.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class WorkerService {
    private final PlayerService playerService;
    private final BoardService boardService;
    private final GameService gameService;

    @Autowired
    public WorkerService(PlayerService playerService, BoardService boardService, GameService gameService) {
       this.playerService = playerService;
       this.boardService = boardService;
       this.gameService = gameService;
    }


    //  convert coordinates to fieldNum
    private int coordsToId(int x, int y) {
        return (x * 5 + y);
    }

    // move function: dest is fieldNum
    public ResponseEntity<Worker> moveTo(long gameId, long playerId, int workerId, int dest) {

        //  find the destination field
        Player player = playerService.getPlayer(playerId);
        Board board = boardService.getBoard(gameId);
        List<Field>  allFields = board.getAllFields();
        Field dest_field = allFields.get(dest);
        //  Field depart_field

        // find the worker to be updated
        Worker worker;
        if (workerId == 1) {
             worker = player.getWorker1();
        }else if (workerId == 2){
           worker = player.getWorker2();
        }else{
            return new ResponseEntity<Worker>( HttpStatus.NOT_FOUND);
        }

//        // check if the dest is legal
//        if (moveTo(dest_field))
//            return new ResponseEntity<Worker>(HttpStatus.CONFLICT);
//        }
//
        return new ResponseEntity<Worker>(worker, HttpStatus.OK);
    }


//
//    public Worker createWorker(Worker newWorker) {
//        //initial position of a worker
//        newWorker.setXCoordinate(-1);
//        newWorker.setYCoordinate(-1);
//        workerRepository.save(newWorker);
//        return newWorker;
//    }
//
//
    public ResponseEntity<Worker> placeWorker(long gameId, long playerId, int workerId, int dest) {

        //TODO: Return doesnt work, don't know why
        Player player = playerService.getPlayer(playerId);
        Board board = boardService.getBoard(gameId);
        List<Field>  allFields = board.getAllFields();
        Field dest_field = allFields.get(dest);

        //JUWE: check if dest field has an occupier worker
        if(dest_field.getOccupier()!=null){
            return new ResponseEntity<Worker>(dest_field.getOccupier(), HttpStatus.CONFLICT);
        }
        else{
            //JUWE: allocate which worker has to be updated
            if (workerId == 1) {
                Worker worker1 = player.getWorker1();
                worker1.setPosition(dest);
                return new ResponseEntity<Worker>(worker1, HttpStatus.OK);
            }else if (workerId == 2){
                Worker worker2 = player.getWorker2();
                worker2.setPosition(dest);
                return new ResponseEntity<Worker>(worker2, HttpStatus.OK);
            }else {
                return new ResponseEntity<Worker>(HttpStatus.NOT_FOUND);
            }
        }
    }
//
//
//
//
//    public ResponseEntity<Field> highlightField(int fieldNum, long gameId) {
//        Field currentField = boardService.getField(fieldNum, gameId);
//        //todo: check available moving options and send them back to frontend
//        long x = currentField.getX_coordinate();
//        long y = currentField.getY_coordinate();
//        return new ResponseEntity<Field>(currentField, HttpStatus.OK);
//
//    }
}
