package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs19.constant.GodCards;
import ch.uzh.ifi.seal.soprafs19.entity.*;

import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs19.repository.WorkerNormalRepository;
import ch.uzh.ifi.seal.soprafs19.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.*;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class WorkerService {
    private final PlayerService playerService;
    private final BoardService boardService;
    private final GameService gameService;
    private final WorkerNormalRepository workerNormalRepository;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final RecordService recordService;

    @Autowired
    public WorkerService(WorkerNormalRepository workerNormalRepository,PlayerService playerService, BoardService boardService, GameService gameService, GameRepository gameRepository, PlayerRepository playerRepository, RecordService recordService) {
        this.playerService = playerService;
        this.boardService = boardService;
        this.gameService = gameService;
        this.workerNormalRepository = workerNormalRepository;
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.recordService = recordService;
    }


    //  convert coordinates to fieldNum
    private int coordsToId(int x, int y) {
        return (x * 5 + y);
    }

    public ResponseEntity<Integer> placeWorker(long gameId, int workerId, int dest){
        Board board = boardService.getBoard(gameId);
        WorkerNormal placingWorker = workerNormalRepository.findById(workerId);
        Field fieldToPlace = boardService.getField(dest, gameId);
        if(fieldToPlace.getOccupier() == null) {
            fieldToPlace.setOccupier(placingWorker);
            placingWorker.setPosition(dest);
            Game currentGame = gameService.getGame(gameId).getBody();
            workerNormalRepository.save(placingWorker);
            gameRepository.save(currentGame);
            if (currentGame.getPlayer1().getWorker1() == placingWorker && currentGame.getPlayer1().getWorker2().getPosition() == -1) {
                currentGame.setGameStatus(GameStatus.Move1);
            } else if (currentGame.getPlayer1().getWorker2() == placingWorker && currentGame.getPlayer1().getWorker1().getPosition() == -1) {
                currentGame.setGameStatus(GameStatus.Move1);
            } else if (currentGame.getPlayer1().getWorker1().getPosition() != -1 && currentGame.getPlayer1().getWorker2().getPosition() != -1
                    && currentGame.getPlayer2().getWorker1().getPosition() == -1 && currentGame.getPlayer2().getWorker2().getPosition() == -1) {
                currentGame.setGameStatus(GameStatus.Move2);
            } else if (currentGame.getPlayer2().getWorker1() == placingWorker && currentGame.getPlayer2().getWorker2().getPosition() == -1) {
                currentGame.setGameStatus(GameStatus.Move2);
            } else if (currentGame.getPlayer2().getWorker2() == placingWorker && currentGame.getPlayer2().getWorker1().getPosition() == -1) {
                currentGame.setGameStatus(GameStatus.Move2);
            } else if (currentGame.getPlayer2().getWorker1().getPosition() != -1 && currentGame.getPlayer2().getWorker2().getPosition() != -1) {
                currentGame.setGameStatus(GameStatus.Move1);
            }
            boardService.updateBoard(board);
            playerRepository.save(currentGame.getPlayer1());
            workerNormalRepository.save(placingWorker);
            gameRepository.save(currentGame);

            //  do recording
            recordService.addState(gameId, board);

            return new ResponseEntity<Integer>(dest, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<List<Integer>> highlightFieldMove(int fieldNum, long gameId){
        Field currentField = boardService.getField(fieldNum, gameId);
        Board board = boardService.getBoard(gameId);
        List<Integer> highlightedFields = new ArrayList<Integer>();
        WorkerNormal movingWorker = currentField.getOccupier();
        int x = currentField.getX_coordinate();
        int y = currentField.getY_coordinate();
        int possibleXCoordinates[] = {x-1, x, x+1};
        int possibleYCoordinates[] = {y-1, y, y+1};
        int initialHeight = currentField.getHeight();

        for(int i = 0; i <= 2; i++){
            for(int j = 0; j <= 2; j++){
                int xToCheck = possibleXCoordinates[i];
                int yToCheck = possibleYCoordinates[j];

                if(xToCheck <= 4 && yToCheck <= 4 && xToCheck >= 0 && yToCheck >= 0) {
                    Field highlightToMove = boardService.getField(coordsToId(xToCheck,yToCheck), gameId);
                    int newHeight = highlightToMove.getHeight();
                    if(highlightToMove.getHeight() != 4 && highlightToMove.getOccupier() == null && newHeight - initialHeight <= 1){
                        highlightedFields.add(highlightToMove.getFieldNum());
                    } }
                //DA: returns out of bounds error with this statement, somehow initial field does not include itself on its own
                // highlightedFields.remove(fieldNum);
            } }
        if(movingWorker.getGodCard().equals(GodCards.Apollo)){
            moveLikeApollo(highlightedFields, x, y, gameId);
        }
        return new ResponseEntity<List<Integer>>(highlightedFields, HttpStatus.OK);
    }
    public ResponseEntity<List<Integer>> moveLikeApollo(List<Integer> highlightFields, int x, int y, long gameId){
        //DA: check if one of the surrounding fields is occupied, if it is by the opponent --> add fieldNum to List
        Field currentField = boardService.getField(coordsToId(x,y), gameId);
        Long currentPlayersId = currentField.getOccupier().getPlayerId();
        Game currentGame = gameRepository.getById(gameId);
            int possibleXCoordinates[] = {x - 1, x, x + 1};
            int possibleYCoordinates[] = {y - 1, y, y + 1};
            for (int i = 0; i <= 2; i++) {
                for (int j = 0; j <= 2; j++) {
                    int xToCheck = possibleXCoordinates[i];
                    int yToCheck = possibleYCoordinates[j];
                    Field isFieldOccupied = boardService.getField(coordsToId(xToCheck, yToCheck), gameId);
                    if(isFieldOccupied.getOccupier() != null && currentPlayersId != isFieldOccupied.getOccupier().getPlayerId()){
                        highlightFields.add(isFieldOccupied.getFieldNum());
                    }
                }
            }
        return new ResponseEntity<List<Integer>>(highlightFields, HttpStatus.OK);
    }

    public ResponseEntity<List<Integer>> highlightFieldBuild(int fieldNum, long gameId){
        Field currentField = boardService.getField(fieldNum, gameId);
        Board board = boardService.getBoard(gameId);
        List<Integer> highlightedFields = new ArrayList<Integer>();
        int x = currentField.getX_coordinate();
        int y = currentField.getY_coordinate();
        int possibleXCoordinates[] = {x-1, x, x+1};
        int possibleYCoordinates[] = {y-1, y, y+1};

        for(int i = 0; i <= 2; i++){
            for(int j = 0; j <= 2; j++){
                int xToCheck = possibleXCoordinates[i];
                int yToCheck = possibleYCoordinates[j];

                if(xToCheck <= 4 && yToCheck <= 4 && xToCheck >= 0 && yToCheck >= 0) {
                    Field highlightToBuild = boardService.getField(coordsToId(xToCheck,yToCheck), gameId);
                    if(highlightToBuild.getHeight() != 4 && highlightToBuild.getOccupier() == null){
                        highlightedFields.add(highlightToBuild.getFieldNum());
                    } }
                // highlightedFields.remove(fieldNum);
            } }
        return new ResponseEntity<List<Integer>>(highlightedFields, HttpStatus.OK);
    }
    public ResponseEntity<Integer> moveTo(long gameId, int workerId, int dest){
        Board board = boardService.getBoard(gameId);
        WorkerNormal movingWorker = workerNormalRepository.findById(workerId);
        Field currentField = boardService.getField(movingWorker.getPosition(), gameId);
        Field destination = boardService.getField(dest, gameId);
        Game currentGame = gameService.getGame(gameId).getBody();
        // DA: if condition for all god cards which enable to move to an occupied field //
        if(destination.getOccupier() != null){
            if(movingWorker.getGodCard().equals(GodCards.Apollo)){
                WorkerNormal tempWorker = destination.getOccupier();
                destination.setOccupier(movingWorker);
                currentField.setOccupier(tempWorker);
            }
        }
        // DA: else, when field to move to is not occupied //
        else {
            destination.setOccupier(movingWorker);
            movingWorker.setPosition(destination.getFieldNum());
            currentField.setOccupier(null);
        }
        if(currentGame.getGameStatus()==GameStatus.Move1 && currentGame.getGameMode().equals(GameMode.NORMAL)){
            currentGame.setGameStatus(GameStatus.Build1);
        }
        else if(currentGame.getGameStatus() == GameStatus.Move2 && currentGame.getGameMode().equals(GameMode.NORMAL)){
            currentGame.setGameStatus(GameStatus.Build2);
        }
        // below youll find the conditions for the moving of Artemis
        if(currentGame.getGameMode().equals(GameMode.GOD) && workerNormalRepository.findById(workerId).getGodCard().equals(GodCards.Artemis)) {
            int i = 0;
             if (currentGame.getGameStatus() == GameStatus.Move1) {
                while (i < 2) {
                    currentGame.setGameStatus(GameStatus.Move1);
                    i++;
                }
            }
            else if(currentGame.getGameStatus().equals(GameStatus.Move2)){
                 while (i < 2) {
                     currentGame.setGameStatus(GameStatus.Move1);
                     i++;
                 }
             }
            movingWorker.setGodCard(GodCards.None);
            currentGame.setGameStatus(GameStatus.Build2);
        }

        gameRepository.save(currentGame);
        workerNormalRepository.save(movingWorker);

        WinningCondition(gameId, currentField.getFieldNum(), dest, workerId);

        boardService.updateBoard(board);

        return new ResponseEntity<Integer>(destination.getFieldNum(), HttpStatus.OK);
    }

    public ResponseEntity<String> build(long gameId, int fieldNum){
        Board board = boardService.getBoard(gameId);
        Field currentField = boardService.getField(fieldNum, gameId);
        Game currentGame = gameService.getGame(gameId).getBody();

        if(currentGame.getGameStatus() == GameStatus.Build1){
            currentGame.setGameStatus(GameStatus.Move2);
        }
        else if(currentGame.getGameStatus() == GameStatus.Build2){
            currentGame.setGameStatus(GameStatus.Move1);
        }

        int h = currentField.getHeight();
        currentField.setHeight(h + 1);

        boardService.updateBoard(board);

        return new ResponseEntity<String>(HttpStatus.OK);
    }
    public ResponseEntity<Boolean> WinningCondition(long gameId, int currentFieldNum, int destFieldNum, int workerId) {
        int h1 = boardService.getField(currentFieldNum, gameId).getHeight();
        int h2 = boardService.getField(destFieldNum, gameId).getHeight();

       Game  currentGame = gameRepository.getById(gameId);

        WorkerNormal winningWorker = workerNormalRepository.findById(workerId);

        if (h1 == 2 && h2 == 3) {
            if (winningWorker.getGodCard() == GodCards.Pan) {
                if (h1 - h2 >= 2) {
                    winningWorker.setIsWinner(true);
                    //  DO RECORDING
                    recordService.findById(gameId).setIsDone(true);
                    return new ResponseEntity<Boolean>(winningWorker.getIsWinner(), HttpStatus.OK);
                }
            }
            if(currentGame.getPlayer1().getWorker1() == winningWorker || currentGame.getPlayer1().getWorker2() == winningWorker){
                currentGame.setGameStatus(GameStatus.Winner1);
                gameRepository.save(currentGame);
            }
            else{
                currentGame.setGameStatus(GameStatus.Winner2);
                gameRepository.save(currentGame);
            }
            winningWorker.setIsWinner(true);

            //  DO RECORDING
            recordService.findById(gameId).setIsDone(true);
            return new ResponseEntity<Boolean>(winningWorker.getIsWinner(), HttpStatus.OK);
        }
        return new ResponseEntity<Boolean>(false, HttpStatus.OK);
    }
}
