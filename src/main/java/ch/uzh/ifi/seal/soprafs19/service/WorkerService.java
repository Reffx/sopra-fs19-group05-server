package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.Color;
import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.entity.*;
import ch.uzh.ifi.seal.soprafs19.repository.FieldRepository;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.repository.PlayfieldRepository;
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
//    private final PlayfieldRepository playfieldRepository;
//    private final FieldRepository fieldRepository;
//    //JuWe: added worker and game repo to handle movements of workers for each player
//    private final WorkerRepository workerRepository;
//    private final GameRepository gameRepository;
//    private final GameService gameService;
//    private final FieldService fieldService;
//
//    @Autowired
//    public WorkerService(PlayfieldRepository playfieldRepository, FieldRepository fieldRepository, WorkerRepository workerRepository, GameRepository gameRepository, GameService gameService, FieldService fieldService) {
//        this.playfieldRepository = playfieldRepository;
//        this.fieldRepository = fieldRepository;
//        //JuWe: added repos and gameservice to handle worker movements
//        this.workerRepository = workerRepository;
//        this.gameRepository = gameRepository;
//        this.gameService = gameService;
//        this.fieldService = fieldService;
//    }
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
//    public ResponseEntity<Worker> placeWorker(long fieldId, long id, long gameId) {
//
//        // if the state of a field is occupied then an conflict error is raised otherwise worker position gets updated and field status is set to occupied
//        Playfield playfield = playfieldRepository.findById(gameId);
//        Field placingField = fieldRepository.findByFieldId(fieldId);
//        Worker worker = workerRepository.findById(id);
//        if ((!placingField.getOccupation())) {
//            worker.setXCoordinate(placingField.getX_coordinate());
//            worker.setYCoordinate(placingField.getY_coordinate());
//            placingField.setOccupation(true);
//        } else {
//            return new ResponseEntity<Worker>(HttpStatus.CONFLICT);
//        }
//        return new ResponseEntity<Worker>(worker, HttpStatus.OK);
//    }
//
//
//
//
//    public ResponseEntity<Field> highlightField(long fieldId) {
//        Field currentField = fieldRepository.findByFieldId(fieldId);
//        long x = currentField.getX_coordinate();
//        long y = currentField.getY_coordinate();
//        return new ResponseEntity<Field>(currentField, HttpStatus.OK);
//
//    }
}
