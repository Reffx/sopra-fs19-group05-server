//package ch.uzh.ifi.seal.soprafs19.service;
//
//import ch.uzh.ifi.seal.soprafs19.entity.*;
//
//import ch.uzh.ifi.seal.soprafs19.repository.WorkerRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//
////@Service
////@Transactional
//public class WorkerService {
//    private final WorkerRepository workerRepository;
//    private final PlayfieldService playfieldService;
//
//    @Autowired
//    public WorkerService(WorkerRepository workerRepository, PlayfieldService playfieldService) {
//       this.workerRepository = workerRepository;
//       this.playfieldService = playfieldService;
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
//}
