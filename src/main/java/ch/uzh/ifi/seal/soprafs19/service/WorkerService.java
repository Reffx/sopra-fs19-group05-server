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
    public ResponseEntity<WorkerNormal> moveTo(long gameId, long playerId, int workerId, int dest) {

        //  find the destination field
        Player player = playerService.getPlayer(playerId);
        Board board = boardService.getBoard(gameId);
        List<Field>  allFields = board.getAllFields();
        Field dest_field = allFields.get(dest);
        //  Field depart_field

        // find the worker to be updated
        WorkerNormal worker;
        if (workerId == 1) {
             worker = player.getWorker1();
        }else if (workerId == 2){
           worker = player.getWorker2();
        }else{
            return new ResponseEntity<WorkerNormal>( HttpStatus.NOT_FOUND);
        }

//        // check if the dest is legal
//        if (moveTo(dest_field))
//            return new ResponseEntity<Worker>(HttpStatus.CONFLICT);
//        }
//
        return new ResponseEntity<WorkerNormal>(worker, HttpStatus.OK);
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
    public ResponseEntity<WorkerNormal> placeWorker(long gameId, long playerId, int workerId, int dest) {

        //TODO: Return doesnt work, don't know why
        Player player = playerService.getPlayer(playerId);
        Board board = boardService.getBoard(gameId);
        List<Field>  allFields = board.getAllFields();
        Field dest_field = allFields.get(dest);

        //JUWE: check if dest field has an occupier worker
        if(dest_field.getOccupier()!=null){
            return new ResponseEntity<WorkerNormal>(dest_field.getOccupier(), HttpStatus.CONFLICT);
        }
        else{
            //JUWE: allocate which worker has to be updated
            if (workerId == 1) {
                WorkerNormal worker1 = player.getWorker1();
                worker1.setPosition(dest);
                dest_field.setOccupier(worker1);
                return new ResponseEntity<WorkerNormal>(worker1, HttpStatus.OK);
            }else if (workerId == 2){
                WorkerNormal worker2 = player.getWorker2();
                worker2.setPosition(dest);
                dest_field.setOccupier(worker2);
                return new ResponseEntity<WorkerNormal>(worker2, HttpStatus.OK);
            }else {
                return new ResponseEntity<WorkerNormal>(HttpStatus.NOT_FOUND);
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

       Field highlight1 = boardService.getField(coordsToId(x - 1, y - 1), gameId);
       Field highlight2 = boardService.getField(coordsToId(x - 1, y), gameId);
       Field highlight3 = boardService.getField(coordsToId(x - 1, y + 1), gameId);
       Field highlight4 = boardService.getField(coordsToId(x , y - 1), gameId);
       Field highlight5 = boardService.getField(coordsToId(x , y), gameId);
       Field highlight6 = boardService.getField(coordsToId(x , y + 1), gameId);
       Field highlight7 = boardService.getField(coordsToId(x + 1 , y - 1), gameId);
       Field highlight8 = boardService.getField(coordsToId(x + 1 , y), gameId);
       Field highlight9 = boardService.getField(coordsToId(x + 1 , y + 1), gameId);

        if(highlight1.getHeight() != 4 && highlight1.getOccupier() == null && highlight1 != null){
           highlightedFields.add(highlight1.getFieldNum());
       }
        if(highlight2.getHeight() != 4 && highlight2.getOccupier() == null && highlight2 != null){
            highlightedFields.add(highlight2.getFieldNum());
        }
        if(highlight3.getHeight() != 4 && highlight3.getOccupier() == null && highlight3 != null){
            highlightedFields.add(highlight3.getFieldNum());
        }
        if(highlight4.getHeight() != 4 && highlight4.getOccupier() == null && highlight4 != null){
            highlightedFields.add(highlight4.getFieldNum());
        }
        if(highlight5.getHeight() != 4 && highlight5.getOccupier() == null && highlight5 != null){
            highlightedFields.add(highlight5.getFieldNum());
        }
        if(highlight6.getHeight() != 4 && highlight6.getOccupier() == null && highlight6 != null){
            highlightedFields.add(highlight6.getFieldNum());
        }
        if(highlight7.getHeight() != 4 && highlight7.getOccupier() == null && highlight7 != null){
            highlightedFields.add(highlight7.getFieldNum());
        }
        if(highlight8.getHeight() != 4 && highlight8.getOccupier() == null && highlight8 != null){
            highlightedFields.add(highlight8.getFieldNum());
        }
        if(highlight9.getHeight() != 4 && highlight9.getOccupier() == null && highlight9 != null){
            highlightedFields.add(highlight9.getFieldNum());
        }

        return new ResponseEntity<List<Integer>>(highlightedFields, HttpStatus.OK);
   }
}
