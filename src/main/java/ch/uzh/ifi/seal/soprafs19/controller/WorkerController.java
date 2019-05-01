package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.Player;
import ch.uzh.ifi.seal.soprafs19.entity.Field;
import ch.uzh.ifi.seal.soprafs19.entity.Worker;
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
    @PutMapping("/games/{gameId}/{playerId}/{workerId}/move")
    ResponseEntity<Worker> moveTo(@PathVariable(name = "gameId") long gameId, @PathVariable(name = "playerId") long playerId, @PathVariable(name = "workerId") int workerId, @RequestBody int dest) {
        return workerService.moveTo(gameId, playerId, workerId, dest);
    }

    @GetMapping("games/{gameId}/{fieldNum}/highlight")
    ResponseEntity<List<Integer>> highlightField(@PathVariable(name = "gameId") long gameId, @PathVariable(name = "fieldNum") int fieldNum){
        return workerService.highlightField(fieldNum, gameId);
    }

    //JUWE: Put mapping to place worker on field in initial round, afterwards move to method must be use
    @PutMapping("/games/{gameId}/{playerId}/{workerId}/place")
    ResponseEntity<Worker> placeWorker(@PathVariable(name = "gameId") long gameId, @PathVariable(name = "playerId") long playerId, @PathVariable(name = "workerId") int workerId, @RequestBody int dest) {
        return workerService.placeWorker(gameId, playerId, workerId, dest);
    }

}
