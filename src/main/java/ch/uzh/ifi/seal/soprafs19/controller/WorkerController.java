package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.constant.Color;
import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.entity.Playfield;
import ch.uzh.ifi.seal.soprafs19.entity.Worker;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.service.GameService;
import ch.uzh.ifi.seal.soprafs19.service.WorkerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WorkerController {

    private final WorkerService workerService;

    WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @PutMapping("/games/{gameId}/{playerId}/{workerId}/move")
    ResponseEntity<Worker> moveTo(@PathVariable(name = "gameId") long gameId, @PathVariable(name = "playerId") long playerId, @PathVariable(name = "workerId") int workerId, @RequestBody int dest) {
        return workerService.moveTo(gameId, playerId, workerId, dest);
    }
}
