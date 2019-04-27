package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.entity.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class WorkerService {
    private final BoardService playfieldService;
    private final PlayerService playerService;

    @Autowired
    public WorkerService(BoardService playfieldService, PlayerService playerService) {
       this.playfieldService = playfieldService;
       this.playerService = playerService;
    }


    //  convert coordinates to fieldNum
    private int coordsToId(int x, int y) {
        return (x * 5 + y);
    }

    // move function
    public ResponseEntity<Worker> moveTo(long gameId, long playerId, int workerId, int dest) {
        Player player = playerService.getPlayer(playerId);

        // find the worker to be updated
        Worker worker;
        if (workerId == 1) {
             worker = player.getWorker1();
        }else if (workerId == 2){
           worker = player.getWorker2();
        }else{
            return new ResponseEntity<Worker>( HttpStatus.NOT_FOUND);
        }

        // check if the dest is allowed
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
//    public ResponseEntity<Field> placeWorker(int fieldNum, long id, long gameId) {
//
//       // TODO: check if state of field is occupied, if so send http.conflict, otherwise update position of worker
//        Field placingField = playfieldService.getField(fieldNum, gameId);
//        //just a dumy return value
//        return new ResponseEntity<Field>(placingField, HttpStatus.OK);
//    }
//
//
//
//
//    public ResponseEntity<Field> highlightField(int fieldNum, long gameId) {
//        Field currentField = playfieldService.getField(fieldNum, gameId);
//        //todo: check available moving options and send them back to frontend
//        long x = currentField.getX_coordinate();
//        long y = currentField.getY_coordinate();
//        return new ResponseEntity<Field>(currentField, HttpStatus.OK);
//
//    }
}
