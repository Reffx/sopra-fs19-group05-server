package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.Color;
import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.Player;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional
public class WorkerService {
    private final WorkerRepository workerRepository;
    private final GameService gameService;
    private final PlayerService playerService;
    private final PlayfieldService playfieldService;

    @Autowired
    public WorkerService(WorkerRepository workerRepository, PlayerService playerService, GameService gameService, PlayfieldService playfieldService) {
        this.workerRepository = workerRepository;
        this.playerService = playerService;
        this.gameService = gameService;
        this.playfieldService = playfieldService;
    }


}