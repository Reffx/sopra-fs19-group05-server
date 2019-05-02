package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.Player;
import ch.uzh.ifi.seal.soprafs19.entity.Field;
import ch.uzh.ifi.seal.soprafs19.entity.WorkerNormal;
import ch.uzh.ifi.seal.soprafs19.service.WorkerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WorkerController {

    private final WorkerService workerService;

    WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    //  moveTo function
    @PutMapping("/games/{gameId}/{fieldNum}/{workerId}/move")
    ResponseEntity<Integer> moveTo(@PathVariable(name = "gameId") long gameId, @PathVariable(name = "fieldNum") int fieldNum, @PathVariable(name = "workerId") int workerId) {
        return workerService.moveTo(gameId, workerId, fieldNum);
    }

    @GetMapping("games/{gameId}/{fieldNum}/highlight")
    ResponseEntity<List<Integer>> highlightField(@PathVariable(name = "gameId") long gameId, @PathVariable(name = "fieldNum") int fieldNum){
        return workerService.highlightField(fieldNum, gameId);
    }

    //JUWE: Put mapping to place worker on field in initial round, afterwards move to method must be use
    @PutMapping("/games/{gameId}/{fieldNum}/{workerId}/place")
    ResponseEntity<Integer> placeWorker(@PathVariable(name = "gameId") long gameId, @PathVariable(name = "fieldNum") int fieldNum, @PathVariable(name = "workerId") int workerId) {
        return workerService.placeWorker(gameId, workerId, fieldNum);
    }

}
