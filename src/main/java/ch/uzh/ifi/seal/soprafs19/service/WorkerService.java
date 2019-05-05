package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.entity.*;

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
                dest_field.setOccupier(worker1);
                return new ResponseEntity<Worker>(worker1, HttpStatus.OK);
            }else if (workerId == 2){
                Worker worker2 = player.getWorker2();
                worker2.setPosition(dest);
                dest_field.setOccupier(worker2);
                return new ResponseEntity<Worker>(worker2, HttpStatus.OK);
            }else {
                return new ResponseEntity<Worker>(HttpStatus.NOT_FOUND);
            }
        }
    }

    public ResponseEntity<List<Integer>> highlightField(int fieldNum, long gameId) {
        Field currentField = boardService.getField(fieldNum, gameId);
        Board board = boardService.getBoard(gameId);
        List<Field>  allFields = board.getAllFields();
        //todo: check available moving options and send them back to frontend
        int x = currentField.getX_coordinate();
        int y = currentField.getY_coordinate();

        List<Integer> highlightedFields = new ArrayList<Integer>();

        // DA: check how to handle border fields //
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
           if(highlight1.getHeight() != 4 && highlight1.getOccupier() == null && highlight1 != null){
               highlightedFields.add(highlight1.getFieldNum());
           }
       }
       if(x2 <= 4 && y2 <= 4 && x2 >= 0 && y2 >0) {
           Field highlight2 = boardService.getField(coordsToId(x2, y2), gameId);
           if (highlight2.getHeight() != 4 && highlight2.getOccupier() == null && highlight2 != null) {
               highlightedFields.add(highlight2.getFieldNum());
           }
       }
       if(x3 <= 4 && y3 <= 4 && x3 >= 0 && y3 >0) {
           Field highlight3 = boardService.getField(coordsToId(x3, y3), gameId);
           if (highlight3.getHeight() != 4 && highlight3.getOccupier() == null && highlight3 != null) {
               highlightedFields.add(highlight3.getFieldNum());
           }
       }
       if(x4 <= 4 && y4 <= 4 && x4 >= 0 && y4 >0) {
           Field highlight4 = boardService.getField(coordsToId(x4, y4), gameId);
           if (highlight4.getHeight() != 4 && highlight4.getOccupier() == null && highlight4 != null) {
               highlightedFields.add(highlight4.getFieldNum());
           }
       }
       if(x5 <= 4 && y5 <= 4 && x5 >= 0 && y5 >0) {
           Field highlight5 = boardService.getField(coordsToId(x5, y5), gameId);
           if (highlight5.getHeight() != 4 && highlight5.getOccupier() == null && highlight5 != null) {
               highlightedFields.add(highlight5.getFieldNum());
           }
       }
       if(x6 <= 4 && y6 <= 4 && x6 >= 0 && y6 >0) {
           Field highlight6 = boardService.getField(coordsToId(x6, y6), gameId);
           if (highlight6.getHeight() != 4 && highlight6.getOccupier() == null && highlight6 != null) {
               highlightedFields.add(highlight6.getFieldNum());
           }
       }
       if(x7 <= 4 && y7 <= 4 && x7 >= 0 && y7 >0) {
           Field highlight7 = boardService.getField(coordsToId(x7, y7), gameId);
           if (highlight7.getHeight() != 4 && highlight7.getOccupier() == null && highlight7 != null) {
               highlightedFields.add(highlight7.getFieldNum());
           }
       }
       if(x8 <= 4 && y8 <= 4 && x8 >= 0 && y8 >0) {
           Field highlight8 = boardService.getField(coordsToId(x8, y8), gameId);
           if (highlight8.getHeight() != 4 && highlight8.getOccupier() == null && highlight8 != null) {
               highlightedFields.add(highlight8.getFieldNum());
           }
       }
       return new ResponseEntity<List<Integer>>(highlightedFields, HttpStatus.OK);
   }
}
