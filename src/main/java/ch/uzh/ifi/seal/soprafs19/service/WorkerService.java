package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs19.constant.GodCards;
import ch.uzh.ifi.seal.soprafs19.entity.*;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs19.repository.WorkerNormalRepository;
import org.hibernate.jdbc.Work;
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
    private final WorkerNormalRepository workerNormalRepository;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;


    @Autowired
    public WorkerService(WorkerNormalRepository workerNormalRepository, PlayerService playerService, BoardService boardService, GameService gameService, GameRepository gameRepository, PlayerRepository playerRepository) {
        this.playerService = playerService;
        this.boardService = boardService;
        this.gameService = gameService;
        this.workerNormalRepository = workerNormalRepository;
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }


    //  convert coordinates to fieldNum
    private int coordsToId(int x, int y) {
        return (x * 5 + y);
    }

    public ResponseEntity<Integer> placeWorker(long gameId, int workerId, int dest) {
        Board board = boardService.getBoard(gameId);
        WorkerNormal placingWorker = workerNormalRepository.findById(workerId);
        Field fieldToPlace = boardService.getField(dest, gameId);
        if (fieldToPlace.getOccupier() == null) {
            fieldToPlace.setOccupier(placingWorker);
            placingWorker.setPosition(dest);
            Game currentGame = gameService.getGame(gameId).getBody();
            workerNormalRepository.save(placingWorker);
            gameRepository.save(currentGame);
            if(currentGame.getGameStatus().equals(GameStatus.Move1) && currentGame.getPlayer1().getWorker1().getPosition() != -1
                    && currentGame.getPlayer1().getWorker2().getPosition() != -1){
                currentGame.setGameStatus(GameStatus.Move2);
            }
            else if(currentGame.getGameStatus().equals(GameStatus.Move2) && currentGame.getPlayer2().getWorker1().getPosition() !=-1
                    && currentGame.getPlayer2().getWorker2().getPosition() != -1){
                currentGame.setGameStatus(GameStatus.Move1);
            }
            boardService.updateBoard(board);
            playerRepository.save(currentGame.getPlayer1());
            workerNormalRepository.save(placingWorker);
            gameRepository.save(currentGame);


            return new ResponseEntity<Integer>(dest, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<List<Integer>> highlightFieldMove(int fieldNum, long gameId) {
        Field currentField = boardService.getField(fieldNum, gameId);
        Board board = boardService.getBoard(gameId);
        List<Integer> highlightedFields = new ArrayList<Integer>();
        WorkerNormal movingWorker = currentField.getOccupier();
        Game game = gameRepository.getById(gameId);
        int x = currentField.getX_coordinate();
        int y = currentField.getY_coordinate();
        int possibleXCoordinates[] = {x - 1, x, x + 1};
        int possibleYCoordinates[] = {y - 1, y, y + 1};
        int initialHeight = currentField.getHeight();

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                int xToCheck = possibleXCoordinates[i];
                int yToCheck = possibleYCoordinates[j];

                if (xToCheck <= 4 && yToCheck <= 4 && xToCheck >= 0 && yToCheck >= 0) {
                    Field highlightToMove = boardService.getField(coordsToId(xToCheck, yToCheck), gameId);
                    int newHeight = highlightToMove.getHeight();
                    if (highlightToMove.getHeight() != 4 && highlightToMove.getOccupier() == null && newHeight - initialHeight <= 1) {
                        highlightedFields.add(highlightToMove.getFieldNum());
                    }
                }
                //DA: returns out of bounds error with this statement, somehow initial field does not include itself on its own
                // highlightedFields.remove(fieldNum);
            }
        }
        if(game.getGameMode().equals(GameMode.GOD)) {
            if (movingWorker.getGodCard().equals(GodCards.Apollo) || movingWorker.getGodCard().equals(GodCards.Minotaur)) {
                moveLikeApolloOrMinotaur(highlightedFields, x, y, gameId);
            }
            Player player1 = game.getPlayer1();
            Player player2 = game.getPlayer2();

            if(movingWorker.getPlayerId() != player1.getId()){
                System.out.println("Checkpoint if player 2 is moving");
                if(player1.getWorker1().getGodCard().equals(GodCards.Athena)){
                    Field currentFieldAthena = boardService.getField(player1.getWorker1().getPosition(), gameId);
                    System.out.println("worker 1 player 1has athena");
                    restrictLikeAthenaOrPrometheus(highlightedFields, gameId, currentField, currentFieldAthena);
                    gameService.assignGodCard("Athena", currentFieldAthena.getOccupier().getPlayerId());
                }
                else if (player1.getWorker2().getGodCard().equals(GodCards.Athena)){
                    Field currentFieldAthena = boardService.getField(player1.getWorker2().getPosition(), gameId);
                    restrictLikeAthenaOrPrometheus(highlightedFields, gameId, currentField, boardService.getField(player1.getWorker2().getPosition(), gameId));
                    gameService.assignGodCard("Athena", currentFieldAthena.getOccupier().getPlayerId());
                }
                gameRepository.save(game);
            }
            if(movingWorker.getPlayerId() != player2.getId()) {
                System.out.println("Check for Athena 1");
                if(player2.getWorker1().getGodCard().equals(GodCards.Athena)){
                    System.out.println("Check for Athena 2");
                    Field currentFieldAthena = boardService.getField(player2.getWorker1().getPosition(), gameId);
                    restrictLikeAthenaOrPrometheus(highlightedFields, gameId, currentField, currentFieldAthena);
                    gameService.assignGodCard("Athena", currentFieldAthena.getOccupier().getPlayerId());
                }
                else if (player2.getWorker2().getGodCard().equals(GodCards.Athena)){
                    System.out.println("Check for Athena 3");
                    Field currentFieldAthena = boardService.getField(player2.getWorker2().getPosition(), gameId);
                    restrictLikeAthenaOrPrometheus(highlightedFields, gameId, currentField, currentFieldAthena);
                    gameService.assignGodCard("Athena", currentFieldAthena.getOccupier().getPlayerId());
                }
                gameRepository.save(game);
            }
            if(movingWorker.getGodCard().equals(GodCards.InactiveArtemis) && movingWorker.getOldPosition() != movingWorker.getPosition()){
                highlightedFields.remove(new Integer(movingWorker.getOldPosition()));
            }
            if(movingWorker.getGodCard().equals(GodCards.Prometheus) || movingWorker.getGodCard().equals(GodCards.Hermes)){
                restrictLikeAthenaOrPrometheus(highlightedFields, gameId, currentField, null);
            }
        }
        return new ResponseEntity<List<Integer>>(highlightedFields, HttpStatus.OK);
    }

    public ResponseEntity<List<Integer>> restrictLikeAthenaOrPrometheus(List<Integer> highlightedFields, long gameId, Field currentField, Field currentFieldAthena){
        System.out.println("Checkout 1");
        int initialHeight = currentField.getHeight();
        List<Integer> copyHighlightedFields = new ArrayList<Integer>(highlightedFields);


        for(int i = 0; i < copyHighlightedFields.size(); i++){
            System.out.println(copyHighlightedFields.size());
            Field toRemove = boardService.getField(copyHighlightedFields.get(i), gameId);
            int h = toRemove.getHeight();

            if(h - initialHeight > 0){
                highlightedFields.remove(new Integer(toRemove.getFieldNum()));
            }
        }
        if(currentFieldAthena != null) {
            System.out.println("Checkpoint3");
        }
        return new ResponseEntity<List<Integer>>(highlightedFields, HttpStatus.OK);
    }

    public ResponseEntity<List<Integer>> highlightFieldBuild(int fieldNum, long gameId) {
        Field currentField = boardService.getField(fieldNum, gameId);
        WorkerNormal buildingWorker = currentField.getOccupier();
        Board board = boardService.getBoard(gameId);
        List<Integer> highlightedFields = new ArrayList<Integer>();
        int x = currentField.getX_coordinate();
        int y = currentField.getY_coordinate();
        int possibleXCoordinates[] = {x - 1, x, x + 1};
        int possibleYCoordinates[] = {y - 1, y, y + 1};

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                int xToCheck = possibleXCoordinates[i];
                int yToCheck = possibleYCoordinates[j];

                if (xToCheck <= 4 && yToCheck <= 4 && xToCheck >= 0 && yToCheck >= 0) {
                    Field highlightToBuild = boardService.getField(coordsToId(xToCheck, yToCheck), gameId);
                    if (highlightToBuild.getHeight() != 4 && highlightToBuild.getOccupier() == null) {
                        highlightedFields.add(highlightToBuild.getFieldNum());
                    } } } }
        if(gameRepository.getById(gameId).getGameMode().equals(GameMode.GOD)) {
            //check if demeter is inactive and if old building position is equal to current buidling position
            //if true remove the current building position from the highlight list
            if (buildingWorker.getGodCard().equals(GodCards.InactiveDemeter) && buildingWorker.getBuildingPosition() == buildingWorker.getOldBuildingPosition()) {
                highlightedFields.remove(new Integer(buildingWorker.getBuildingPosition()));
            } }
        return new ResponseEntity<List<Integer>>(highlightedFields, HttpStatus.OK);
    }

    public ResponseEntity<Integer> moveTo(long gameId, int workerId, int dest) {
        Board board = boardService.getBoard(gameId);
        WorkerNormal movingWorker = workerNormalRepository.findById(workerId);
        Field currentField = boardService.getField(movingWorker.getPosition(), gameId);
        Field destination = boardService.getField(dest, gameId);
        Game currentGame = gameService.getGame(gameId).getBody();
        // DA: if condition for all god cards which enable to move to an occupied field //
        //moving for normal mode
        if(currentGame.getGameMode().equals(GameMode.NORMAL)) {
            destination.setOccupier(movingWorker);
            movingWorker.setPosition(destination.getFieldNum());
            currentField.setOccupier(null);
            if (currentGame.getGameStatus() == GameStatus.Move1 && currentGame.getGameMode().equals(GameMode.NORMAL)) {
                currentGame.setGameStatus(GameStatus.Build1);
            }
            else if (currentGame.getGameStatus() == GameStatus.Move2 && currentGame.getGameMode().equals(GameMode.NORMAL)) {
                currentGame.setGameStatus(GameStatus.Build2);
            }
        }
        //moving for god mode, difference between god cards considered
        else if(currentGame.getGameMode().equals(GameMode.GOD)) {
            if(!movingWorker.getGodCard().equals(GodCards.Minotaur) && !movingWorker.getGodCard().equals(GodCards.Apollo)) {
                destination.setOccupier(movingWorker);
                movingWorker.setPosition(destination.getFieldNum());
                currentField.setOccupier(null);
            }
            //HERMES:
            if (movingWorker.getGodCard().equals(GodCards.Hermes)) {
                if (destination.getHeight() - currentField.getHeight() == 0) {
                    if (currentGame.getGameStatus().equals(GameStatus.Move1)) {
                        currentGame.setGameStatus(GameStatus.Move1);
                    } else if (currentGame.getGameStatus().equals(GameStatus.Move2)) {
                        currentGame.setGameStatus(GameStatus.Move2);
                    }
                }
                else {
                    if (currentGame.getGameStatus().equals(GameStatus.Move1)) {
                        currentGame.setGameStatus(GameStatus.Build1);
                    }
                    else if (currentGame.getGameStatus().equals(GameStatus.Move2)) {
                        currentGame.setGameStatus(GameStatus.Build2);
                    }
                }
            }
            //INACTIVE HERMES:
            else if (movingWorker.getGodCard().equals(GodCards.InactiveHermes)) {
                if (currentGame.getGameStatus().equals(GameStatus.Move1)) {
                    currentGame.setGameStatus(GameStatus.Build1);
                }
                else if (currentGame.getGameStatus().equals(GameStatus.Move2)) {
                    currentGame.setGameStatus(GameStatus.Build2);
                }
            }
            // APOLLO / MINOTAUR
            if (movingWorker.getGodCard().equals(GodCards.Apollo) || movingWorker.getGodCard().equals(GodCards.Minotaur)) {
                if (destination.getOccupier() != null) {
                    if(currentGame.getGameStatus().equals(GameStatus.Move1)){
                        currentGame.setGameStatus(GameStatus.Build1);
                    }
                    else if(currentGame.getGameStatus().equals(GameStatus.Move2)){
                        currentGame.setGameStatus(GameStatus.Build2);
                    }
                    if (movingWorker.getGodCard().equals(GodCards.Apollo)) {
                        System.out.println("Check for Apollo: "+movingWorker.getPosition());
                        WorkerNormal tempWorker = destination.getOccupier();
                        destination.setOccupier(movingWorker);
                        currentField.setOccupier(tempWorker);
                        movingWorker.setPosition(destination.getFieldNum());
                        tempWorker.setPosition(currentField.getFieldNum());

                        System.out.println("Check for Apollo 2  "+movingWorker.getPosition() + " "+ tempWorker.getPosition());
                    }
                    if (movingWorker.getGodCard().equals(GodCards.Minotaur)) {
                        int x = destination.getX_coordinate() + (destination.getX_coordinate() - currentField.getX_coordinate());
                        System.out.println("X Coordinate: " + x);
                        int y = destination.getY_coordinate() + (destination.getY_coordinate() - currentField.getY_coordinate());
                        System.out.println("Y Coordinate: " + y);
                        Field pushField = boardService.getField(coordsToId(x, y), gameId);
                        WorkerNormal pushWorker = destination.getOccupier();

                        System.out.println("Check for Minotaur: "+movingWorker.getPosition());

                        pushField.setOccupier(pushWorker);
                        currentField.setOccupier(null);
                        destination.setOccupier(movingWorker);
                        movingWorker.setPosition(destination.getFieldNum());
                        pushWorker.setPosition(pushField.getFieldNum());
                        System.out.println("Check for Minotaur 2"+movingWorker.getPosition() + " "+ pushWorker.getPosition());

                    }
                }
                else if(destination.getOccupier() == null){
                    destination.setOccupier(movingWorker);
                    movingWorker.setPosition(destination.getFieldNum());
                    currentField.setOccupier(null);
                    if(currentGame.getGameStatus().equals(GameStatus.Move1)){
                        currentGame.setGameStatus(GameStatus.Build1);
                    }
                    else if(currentGame.getGameStatus().equals(GameStatus.Move2)){
                        currentGame.setGameStatus(GameStatus.Build2);
                    }
                }
            }
            // if worker moved up set inactive to active //
            // INACTIVE ATHENA:
            if (movingWorker.getGodCard().equals(GodCards.InactiveAthena)) {
                System.out.println("Checkpoint InactiveAthena triggered!!!!!!!!!!!!!!!!!!!!!!!!!");
                if (destination.getHeight() - currentField.getHeight() > 0) {
                    gameService.assignGodCard("InactiveAthena", movingWorker.getPlayerId());
                }
            }
            if (movingWorker.getGodCard().equals(GodCards.Prometheus)) {
                destination.setOccupier(movingWorker);
                movingWorker.setPosition(destination.getFieldNum());
                currentField.setOccupier(null);
                gameService.assignGodCard("InactivePrometheus", movingWorker.getPlayerId());
                if (currentGame.getGameStatus().equals(GameStatus.Build1)) {
                    currentGame.setGameStatus(GameStatus.Move1);
                } else if (currentGame.getGameStatus().equals(GameStatus.Build2)) {
                    currentGame.setGameStatus(GameStatus.Move2);
                }
            }
            if (currentGame.getGameStatus().equals(GameStatus.Move1)) {
                //SPECIAL CONDITIONS FOR ARTEMIS AND PROMETHEUS
                if(movingWorker.getGodCard().equals(GodCards.InactiveArtemis) || movingWorker.getGodCard().equals(GodCards.InactivePrometheus)  || (!movingWorker.getGodCard().equals(GodCards.Hermes) && !movingWorker.getGodCard().equals(GodCards.Artemis)) && !movingWorker.getGodCard().equals(GodCards.Apollo) && !movingWorker.getGodCard().equals(GodCards.Minotaur)) {
                    currentGame.setGameStatus(GameStatus.Build1);
                    movingWorker.setPosition(dest);
                    movingWorker.setOldPosition(dest);
                    System.out.println("Current Position/OldPosition " + movingWorker.getPosition() + "  " + movingWorker.getOldPosition());
                }
                //if Artemis is activated, set GameStatus to Move1 again because Artemis enables double movement
                else if(movingWorker.getGodCard().equals(GodCards.Artemis) || movingWorker.getGodCard().equals(GodCards.Prometheus) ){
                    currentGame.setGameStatus(GameStatus.Move1);
                    movingWorker.setOldPosition(currentField.getFieldNum());
                    gameService.assignGodCard("InactiveArtemis", movingWorker.getPlayerId());
                    System.out.println("Current Position/OldPosition " + movingWorker.getPosition() + "  " + movingWorker.getOldPosition());
                }
            } else if (currentGame.getGameStatus().equals(GameStatus.Move2)) {

                if(movingWorker.getGodCard().equals(GodCards.InactiveArtemis) || movingWorker.getGodCard().equals(GodCards.InactivePrometheus)  || (!movingWorker.getGodCard().equals(GodCards.Hermes) && !movingWorker.getGodCard().equals(GodCards.Artemis)) && !movingWorker.getGodCard().equals(GodCards.Apollo) && !movingWorker.getGodCard().equals(GodCards.Minotaur)) {
                    //if inactive artemis set old position = destination and current position = destination
                    currentGame.setGameStatus(GameStatus.Build2);
                    movingWorker.setPosition(dest);
                    movingWorker.setOldPosition(dest);
                }
                //if Artemis is activated, set GameStatus to Move1 again because Artemis enables double movement
                else if(movingWorker.getGodCard().equals(GodCards.Artemis) || movingWorker.getGodCard().equals(GodCards.Prometheus) ){
                    //if artemis active, set old position = current position to check later for highlight fields if current and old postion
                    //are different
                    currentGame.setGameStatus(GameStatus.Move2);
                    movingWorker.setOldPosition(currentField.getFieldNum());
                    gameService.assignGodCard("InactiveArtemis", movingWorker.getPlayerId());
                }
            }
        }


        gameRepository.save(currentGame);


        WinningCondition(gameId, currentField.getFieldNum(), dest, workerId);

        boardService.updateBoard(board);
        return new ResponseEntity<Integer>(destination.getFieldNum(), HttpStatus.OK);
    }

    public ResponseEntity<String> build(long gameId, int fieldNum, int workerId) {
        Board board = boardService.getBoard(gameId);
        Field currentField = boardService.getField(fieldNum, gameId);
        Game currentGame = gameService.getGame(gameId).getBody();
        WorkerNormal buildingWorker = workerNormalRepository.findById(workerId);
        System.out.println("TEst: "+buildingWorker.getWorkerId());
        int h = currentField.getHeight();
        System.out.println("Checkpoint 1");
        // check for normal mode and current game status
        if(currentGame.getGameMode().equals(GameMode.NORMAL)){
            if (currentGame.getGameStatus().equals(GameStatus.Build1)) {
                currentGame.setGameStatus(GameStatus.Move2);
            }
            // check for normal mode and current game status
            else if (currentGame.getGameStatus().equals(GameStatus.Build2 )) {
                currentGame.setGameStatus(GameStatus.Move1);
            }
            currentField.setHeight(h + 1);
        }
        // check for god mode and current game status, update it accordingly
        if(currentGame.getGameMode().equals(GameMode.GOD)){
            if(!buildingWorker.getGodCard().equals(GodCards.Hephaestus)) {
                currentField.setHeight(h + 1);
            }
            //Prometheus moving condition//
            if(buildingWorker.getGodCard().equals(GodCards.Prometheus) && currentGame.getGameStatus().equals(GameStatus.Build1)){
                currentField.setHeight(h + 1);
                currentGame.setGameStatus(GameStatus.Move1);
            }
            if(buildingWorker.getGodCard().equals(GodCards.Prometheus) && currentGame.getGameStatus().equals(GameStatus.Build2)){
                currentField.setHeight(h + 1);
                currentGame.setGameStatus(GameStatus.Move2);
            }
            //if demeter is inactive, update building position to fieldnum --> old building position
            // and current building position are different --> used to remove for highlight list
            if (currentGame.getGameStatus() == GameStatus.Build1 && !buildingWorker.getGodCard().equals(GodCards.Demeter) && !buildingWorker.getGodCard().equals(GodCards.Hephaestus)) {
                buildingWorker.setBuildingPosition(fieldNum);
                currentGame.setGameStatus(GameStatus.Move2);

            }
            else if (currentGame.getGameStatus() == GameStatus.Build2  && !buildingWorker.getGodCard().equals(GodCards.Demeter) && !buildingWorker.getGodCard().equals(GodCards.Hephaestus)) {
                currentGame.setGameStatus(GameStatus.Move1);
                buildingWorker.setBuildingPosition(fieldNum);
            }

            // DA: if godCard Atlas activated just set height to 4 //
            if (buildingWorker.getGodCard().equals(GodCards.Atlas)) {
                System.out.println("Atlas here, got triggered");
                currentField.setHeight(h=4);
                System.out.println(currentField.getHeight());
                gameService.assignGodCard("InactiveAtlas", buildingWorker.getPlayerId());
                System.out.println("Checkpoint3");
            }
            //check for Demeter Build1 --> change to second building phase, set old and current building position to fieldnum
            if (buildingWorker.getGodCard().equals(GodCards.Demeter) && currentGame.getGameStatus().equals(GameStatus.Build1)) {
                currentGame.setGameStatus(GameStatus.Build1);
                buildingWorker.setBuildingPosition(fieldNum);
                buildingWorker.setOldBuildingPosition(fieldNum);
                gameService.assignGodCard("InactiveDemeter", buildingWorker.getPlayerId());
                currentField.setHeight(h + 1);
            }
            //check for Demeter Build1 --> change to second building phase, set old and current building position to fieldnum
            if (buildingWorker.getGodCard().equals(GodCards.Demeter) && currentGame.getGameStatus().equals(GameStatus.Build2)) {
                currentGame.setGameStatus(GameStatus.Build2);
                buildingWorker.setBuildingPosition(fieldNum);
                buildingWorker.setOldBuildingPosition(fieldNum);
                gameService.assignGodCard("InactiveDemeter", buildingWorker.getPlayerId());
                currentField.setHeight(h + 1);
            }
            if(buildingWorker.getGodCard().equals(GodCards.Hephaestus) && currentGame.getGameStatus().equals(GameStatus.Build1)){
                System.out.println("Hephaestus check");
                if(currentField.getHeight() < 2){
                    System.out.println("CurrentField Height <2: "+ h);
                    currentField.setHeight(h + 2);
                    gameService.assignGodCard("InactiveHephaestus", buildingWorker.getPlayerId());
                    currentGame.setGameStatus(GameStatus.Move2);
                }
                else {
                    System.out.println("Hephaestus 1 check");
                    System.out.println("CurrentField Height >2: "+ h);
                    currentField.setHeight(h + 1);
                    gameService.assignGodCard("InactiveHephaestus", buildingWorker.getPlayerId());
                    currentGame.setGameStatus(GameStatus.Move2);
                } }
            if(buildingWorker.getGodCard().equals(GodCards.Hephaestus) && currentGame.getGameStatus().equals(GameStatus.Build2)) {
                System.out.println("Hephaestus 2 check");
                if (currentField.getHeight() < 2) {
                    currentField.setHeight(h + 2);
                    gameService.assignGodCard("InactiveHephaestus", buildingWorker.getPlayerId());
                    currentGame.setGameStatus(GameStatus.Move1);
                } else {
                    System.out.println("Hephaestus 3 check");
                    currentField.setHeight(h + 1);
                    gameService.assignGodCard("InactiveHephaestus", buildingWorker.getPlayerId());
                    currentGame.setGameStatus(GameStatus.Move1);
                }
            }

        }

        gameRepository.save(currentGame);
        boardService.updateBoard(board);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    public ResponseEntity<Boolean> WinningCondition(long gameId, int currentFieldNum, int destFieldNum, int workerId) {
        int h1 = boardService.getField(currentFieldNum, gameId).getHeight();
        int h2 = boardService.getField(destFieldNum, gameId).getHeight();

        Game currentGame = gameRepository.getById(gameId);

        WorkerNormal winningWorker = workerNormalRepository.findById(workerId);
        Player winningPlayer = playerRepository.getById(winningWorker.getPlayerId());
        WorkerNormal winningWorker2;
        if(winningWorker == winningPlayer.getWorker1()){
            winningWorker2 = winningPlayer.getWorker2();
        }
        else {
            winningWorker2 = winningPlayer.getWorker1();
        }

        if (h1 == 2 && h2 == 3) {

            if (currentGame.getPlayer1().getWorker1() == winningWorker || currentGame.getPlayer1().getWorker2() == winningWorker) {
                currentGame.setGameStatus(GameStatus.Winner1);
                gameRepository.save(currentGame);
            } else {
                currentGame.setGameStatus(GameStatus.Winner2);
                gameRepository.save(currentGame);
            }
            winningWorker.setIsWinner(true);

            return new ResponseEntity<Boolean>(winningWorker.getIsWinner(), HttpStatus.OK);
        }
        if(currentGame.getGameMode().equals(GameMode.GOD)) {
            if (winningWorker.getGodCard() == GodCards.Pan) {
                if (h1 - h2 >= 2) {
                    if(currentGame.getPlayer1().getId() == winningWorker.getPlayerId()){
                        currentGame.setGameStatus(GameStatus.Winner1);
                    }
                    else if(currentGame.getPlayer2().getId() == winningWorker.getPlayerId()){
                        currentGame.setGameStatus(GameStatus.Winner2);
                    }
                    return new ResponseEntity<Boolean>(winningWorker.getIsWinner(), HttpStatus.OK);
                } } }
        if(highlightFieldMove(winningWorker.getPosition(), gameId).getBody().size() == 0 && highlightFieldMove(winningWorker2.getPosition(), gameId).getBody().size() == 0){
            if(currentGame.getGameStatus().equals(GameStatus.Move1)) {
                currentGame.setGameStatus(GameStatus.Winner2);
            }
            else if(currentGame.getGameStatus().equals(GameStatus.Move2)){
                currentGame.setGameStatus(GameStatus.Winner1);
            }
        }
        return new ResponseEntity<Boolean>(false, HttpStatus.OK);
    }

    public ResponseEntity<List<Integer>> moveLikeApolloOrMinotaur(List<Integer> highlightFields, int x, int y, long gameId) {
        //DA: check if one of the surrounding fields is occupied, if it is by the opponent --> add fieldNum to List
        Field currentField = boardService.getField(coordsToId(x, y), gameId);
        Long currentPlayersId = currentField.getOccupier().getPlayerId();
        Game currentGame = gameRepository.getById(gameId);
        Field initialField = boardService.getField(coordsToId(x, y), gameId);
        int possibleXCoordinates[] = {x - 1, x, x + 1};
        int possibleYCoordinates[] = {y - 1, y, y + 1};
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                int xToCheck = possibleXCoordinates[i];
                int yToCheck = possibleYCoordinates[j];
                if(xToCheck <= 4 && yToCheck <= 4 && xToCheck >= 0 && yToCheck >= 0) {
                    Field isFieldOccupied = boardService.getField(coordsToId(xToCheck, yToCheck), gameId);


                    if (isFieldOccupied.getOccupier() != null && currentPlayersId != isFieldOccupied.getOccupier().getPlayerId()) {
                        // DA: if minotaur, additionally check if occupied field is no border field +
                        // check if field to which opponents worker is pushed to is neither occupied nor has a dome //
                        if (currentField.getOccupier().getGodCard().equals(GodCards.Minotaur)) {
                            int pushToXCoordinate = xToCheck + (xToCheck - x);
                            int pushToYCoordinate = yToCheck + (yToCheck - y);
                            if(pushToXCoordinate <= 4 && pushToYCoordinate <= 4 && pushToXCoordinate >= 0 && pushToYCoordinate >= 0){
                                Field fieldToPush = boardService.getField(coordsToId(pushToXCoordinate, pushToYCoordinate), gameId);
                                if (fieldToPush.getOccupier() == null && fieldToPush.getHeight() != 4) {
                                    highlightFields.add(isFieldOccupied.getFieldNum());
                                    highlightFields.remove(new Integer(initialField.getFieldNum()));
                                } }}

                        // DA: if apollo, all condition already check --> add field to list
                        else if (currentField.getOccupier().getGodCard().equals(GodCards.Apollo)) {
                            highlightFields.add(isFieldOccupied.getFieldNum());

                        } } }} }
        return new ResponseEntity<List<Integer>>(highlightFields, HttpStatus.OK);
    }
}