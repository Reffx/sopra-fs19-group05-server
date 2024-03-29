package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.entity.Board;
import ch.uzh.ifi.seal.soprafs19.constant.Color;
import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs19.constant.GodCards;
import ch.uzh.ifi.seal.soprafs19.controller.DuplicateException;
import ch.uzh.ifi.seal.soprafs19.controller.FullLobbyException;
import ch.uzh.ifi.seal.soprafs19.controller.NonExistentGameException;
import ch.uzh.ifi.seal.soprafs19.entity.*;
import ch.uzh.ifi.seal.soprafs19.repository.BoardRepository;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs19.repository.WorkerNormalRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@Transactional
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerService playerService;
    private final WorkerNormalRepository workerNormalRepository;
    private final PlayerRepository playerRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public GameService(GameRepository gameRepository, PlayerService playerService, WorkerNormalRepository workerNormalRepository, PlayerRepository playerRepository, BoardRepository boardRepository) {
        this.gameRepository = gameRepository;
        this.playerService = playerService;
        this.workerNormalRepository = workerNormalRepository;
        this.playerRepository = playerRepository;
        this.boardRepository = boardRepository;
    }

    //public ResponseEntity<>
    //  find all the games
    public Iterable<Game> getGames() {
        return this.gameRepository.findAll();
    }

    //  find all the NORMAL/GOD mode game
    public ResponseEntity<Iterable<Game>> getModeGames(GameMode gameMode) {
        if(gameRepository.findByGameMode(gameMode) != null) {
            return new ResponseEntity<Iterable<Game>>(gameRepository.findByGameMode(gameMode), HttpStatus.FOUND);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //  find a game by id
    public ResponseEntity<Game> getGame(long gameId) {
        if(gameRepository.findById(gameId).isPresent()) {
            return new ResponseEntity<Game>(gameRepository.findById(gameId).get(), HttpStatus.FOUND);
        }
        else{
            throw new NonExistentGameException("Game was not found!");
        }
    }

    //  create a game
    public Game createGame(Game game) {
        //added if statement to check if the same user wants to create multiple games
        if(playerRepository.findByUsername(game.getPlayer1().getUsername())!= null){
            throw new DuplicateException("You have already created a game!");
        }
        Player player1 = game.getPlayer1();   //  set the player1 as the creator
        //  save the player1 to the playerRepository
        //  save first to get gameId
        //game.setPlayer1(player1);
        WorkerNormal worker1 = new WorkerNormal();
        worker1.setGodCard(GodCards.None);
        //System.out.println(worker1.getWorkerId());
        workerNormalRepository.save(worker1);
        WorkerNormal worker2 = new WorkerNormal();
        worker2.setGodCard(GodCards.None);
        workerNormalRepository.save(worker2);
        //System.out.println(worker2.getWorkerId());
        // worker1 = workerNormalRepository.findById(0).get();
        //System.out.println(worker1.getWorkerId());
        player1.setWorker1(worker1);
        player1.setWorker2(worker2);
        worker1.setPlayerId(player1.getId());
        worker2.setPlayerId(player1.getId());

        /*WorkerNormal worker2 = new WorkerNormal();
        worker2.setId(2);
        player1.setWorker2(worker2); */
        playerService.savePlayer(player1);
        gameRepository.save(game);

        //  set player gameId
        long gameId = game.getId();
        System.out.println("GameId:"+ gameId);
        player1.setGameId(gameId);
        playerService.savePlayer(player1);

        //  save again to save the gameId in Player
        gameRepository.save(game);

        return game; //   response code:201, frontend fetch the gameId in the body
    }

    //  update a game, add player1 or player2
    public ResponseEntity<Game> joinLobby(Long userId, Long gameId) {
        Game game = gameRepository.findById(gameId).get();
        if(game.getSize()==2){
            throw new FullLobbyException("Not allowed to join full lobby!");
        }
        //  create new player
        Player player = new Player();
        player.setId(userId);

        WorkerNormal worker1 = new WorkerNormal();
        worker1.setGodCard(GodCards.None);
        WorkerNormal worker2 = new WorkerNormal();
        worker2.setGodCard(GodCards.None);
        player.setWorker1(worker1);
        player.setWorker2(worker2);
        worker1.setPlayerId(player.getId());
        worker2.setPlayerId(player.getId());
        workerNormalRepository.save(worker1);
        workerNormalRepository.save(worker2);
        //JUWE: player 2 had no username, easier to have it here than in frontend
        player.setUsername(playerService.getUsername(userId));
        player.setGameId(gameId);
        playerRepository.save(player);

        if (game.getPlayer2() == null) {
            game.setPlayer2(player);
        }
        else{
            game.setPlayer1(player);

        }

        game.setSize(2);
        gameRepository.save(game);
        //JuWe: 01.05.19 changed response type to game, since its easier to understand if a player2 is in the game object
        return new ResponseEntity<Game>(game, HttpStatus.OK);   // response code 200
    }

    //  update a game, remove player1 or player2. if both are null afterwards, delete game
    public ResponseEntity<String> leaveLobby(Long gameId, Long playerId) {
        Game game = gameRepository.getById(gameId);

        //  to find the player in the database and remove it, cascade deletion of player2 in the Game instance
        Player player = playerService.getPlayer(playerId);
        if (game.getPlayer2() == player) {
            game.setPlayer2(null);
            game.setSize(1);
            playerService.deletebyId(playerId);
            gameRepository.save(game);
        }else if (game.getPlayer1() == player && game.getSize() == 2) {
            game.setPlayer1(null);
            playerService.deletebyId(playerId);
            game.setPlayer1(game.getPlayer2());
            game.setPlayer2(null);
            game.setSize(1);
            gameRepository.save(game);
        }
        else if (game.getPlayer1() != null && game.getPlayer2() == null){
            playerService.deletebyId(game.getPlayer1().getId());
            deleteGame(gameId);
        }
        else{
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<String>(HttpStatus.OK);   // response code 200
    }

    //  set player color
    public ResponseEntity<String> setColor(Long gameId, Long playerId, Color color) {
        //  check if there's color conflict
        Game game = gameRepository.getById(gameId);
        Player playerX = playerService.getPlayer(playerId);

        Player player1 = game.getPlayer1();
        Player player2 = game.getPlayer2();

        //JUWE 01.05.19 the first check (if only player1 is in lobby) was missing, thats why color selection worked only after somebody joined the lobby
        if(player1 == playerX && player2 == null){
            player1.setColor(color);
            playerService.savePlayer(player1);
            return new ResponseEntity<String>(color.toString(),HttpStatus.OK);
        }
        else if (player1 == playerX && player2 != null){
            if(player2.getColor() == color){
                throw new DuplicateException("Color already in use by opponent!");
            }
            else{
                player1.setColor(color);
                playerService.savePlayer(player1);
                return new ResponseEntity<String>(color.toString(), HttpStatus.OK);
            }
        }
        else if (player2 == playerX && player1 != null){
            if(player1.getColor() == color){
                throw new DuplicateException("Color already in use by opponent!");
            }
            else {
                player2.setColor(color);
                playerService.savePlayer(player2);
                return new ResponseEntity<String>(color.toString(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }


    //  set player2 status
    public ResponseEntity<String> setStatus(Long gameId, Long playerId) {
        Game currentGame = gameRepository.getById(gameId);
        if(playerRepository.getById(playerId) == null){
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        Player player = playerService.getPlayer(playerId);
        boolean status = player.getStatus();
        player.setStatus(!status);  //  negation of the original status

        playerService.savePlayer(player);

        if(!currentGame.getGameStatus().equals(GameStatus.Start)){
            if(currentGame.getGameStatus().equals(GameStatus.Move1)){
                currentGame.setGameStatus(GameStatus.Build1);
            }
            if(currentGame.getGameStatus().equals(GameStatus.Move2)){
                currentGame.setGameStatus(GameStatus.Build2);
            }
            gameRepository.save(currentGame);
        }
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    //  set beginner JUWE: removed responseEntity because I had problems to call setBeginner in the WorkerService
    public Long setBeginner(Long gameId) {
        if(gameRepository.getById(gameId) == null){
            throw new NonExistentGameException("Game was not found!");
        }
        //  get playerId
        Long player1Id = gameRepository.getById(gameId).getPlayer1().getId();
        Long player2Id = gameRepository.getById(gameId).getPlayer2().getId();

        //  randomly select a player
        Random random = new Random();

        int id_rand = random.nextInt(2);
        if (id_rand == 0) {
            gameRepository.getById(gameId).setGameStatus(GameStatus.Move1);
            return player1Id;
        }
        gameRepository.getById(gameId).setGameStatus(GameStatus.Move2);
        return player2Id;
    }


    //  delete a game. when player1 exit
    public ResponseEntity<String> deleteGame(Long gameId) {
        Game currentGame = gameRepository.getById(gameId);

        if(gameRepository.getById(gameId)== null){
            throw new NonExistentGameException("The game you want to delete couldn't be found in the repository!");
        }
        //check if players are actually in game and not in lobby
        if(currentGame.getGameStatus() != GameStatus.Start){
            Board currentBoard = boardRepository.getById(gameId);
            List<Field> allFields = currentBoard.getAllFields();
            //iterate over field objects to set the occupiers null if there is one
            for( int i = 0; i <= 24; ++i){
                if(allFields.get(i).getOccupier() != null){
                    allFields.get(i).setOccupier(null);
                }
            }
            //delete the board
            boardRepository.deleteById(gameId);
            //delete workers from repo
            workerNormalRepository.deleteById(currentGame.getPlayer1().getWorker1().getWorkerId());
            workerNormalRepository.deleteById(currentGame.getPlayer1().getWorker2().getWorkerId());
            workerNormalRepository.deleteById(currentGame.getPlayer2().getWorker1().getWorkerId());
            workerNormalRepository.deleteById(currentGame.getPlayer2().getWorker2().getWorkerId());

            //delete players from player repo
            playerRepository.deleteById(currentGame.getPlayer1().getId());
            playerRepository.deleteById(currentGame.getPlayer2().getId());

        }
        gameRepository.deleteById(gameId);
        return new ResponseEntity<String>(HttpStatus.OK); // response code: 204
    }

    public ResponseEntity<String> assignGodCard(String godCard, long playerId){
        WorkerNormal worker1 = playerService.getPlayer(playerId).getWorker1();
        WorkerNormal worker2 = playerService.getPlayer(playerId).getWorker2();
        Long gameId = playerService.getPlayer(playerId).getGameId();
        Game currentGame = gameRepository.getById(gameId);

        if(godCard.equals("Pan")){
            worker1.setGodCard(GodCards.Pan);
            worker2.setGodCard(GodCards.Pan);
            workerNormalRepository.save(worker1);
            workerNormalRepository.save(worker2);
        }
        if(godCard.equals("Artemis")){
            worker1.setGodCard(GodCards.Artemis);
            worker2.setGodCard(GodCards.Artemis);
            workerNormalRepository.save(worker1);
            workerNormalRepository.save(worker2);
        }
        if(godCard.equals("InactiveArtemis")){
            worker1.setGodCard(GodCards.InactiveArtemis);
            worker2.setGodCard(GodCards.InactiveArtemis);
            workerNormalRepository.save(worker1);
            workerNormalRepository.save(worker2);
        }
        if(godCard.equals("Apollo")) {
            worker1.setGodCard(GodCards.Apollo);
            worker2.setGodCard(GodCards.Apollo);
            workerNormalRepository.save(worker1);
            workerNormalRepository.save(worker2);
        }
        if(godCard.equals("Atlas")) {
            worker1.setGodCard(GodCards.Atlas);
            worker2.setGodCard(GodCards.Atlas);
            workerNormalRepository.save(worker1);
            workerNormalRepository.save(worker2);
        }
        if(godCard.equals("InactiveAtlas")) {
            worker1.setGodCard(GodCards.InactiveAtlas);
            worker2.setGodCard(GodCards.InactiveAtlas);
            workerNormalRepository.save(worker1);
            workerNormalRepository.save(worker2);
        }
        if(godCard.equals("Minotaur")){
            worker1.setGodCard(GodCards.Minotaur);
            worker2.setGodCard(GodCards.Minotaur);
            workerNormalRepository.save(worker1);
            workerNormalRepository.save(worker2);
        }
        if(godCard.equals("Demeter")){
            worker1.setGodCard(GodCards.Demeter);
            worker2.setGodCard(GodCards.Demeter);
            workerNormalRepository.save(worker1);
            workerNormalRepository.save(worker2);
        }
        if(godCard.equals("InactiveDemeter")){
            worker1.setGodCard(GodCards.InactiveDemeter);
            worker2.setGodCard(GodCards.InactiveDemeter);
            workerNormalRepository.save(worker1);
            workerNormalRepository.save(worker2);
        }
        if(godCard.equals("Athena")){
            worker1.setGodCard(GodCards.InactiveAthena);
            worker2.setGodCard(GodCards.InactiveAthena);
            workerNormalRepository.save(worker1);
            workerNormalRepository.save(worker2);
        }
        if(godCard.equals("InactiveAthena")){
            worker1.setGodCard(GodCards.Athena);
            worker2.setGodCard(GodCards.Athena);
            workerNormalRepository.save(worker1);
            workerNormalRepository.save(worker2);
        }
        if(godCard.equals("Hephaestus")) {
            worker1.setGodCard(GodCards.Hephaestus);
            worker2.setGodCard(GodCards.Hephaestus);
            workerNormalRepository.save(worker1);
            workerNormalRepository.save(worker2);
        }
        if(godCard.equals("InactiveHephaestus")) {
            worker1.setGodCard(GodCards.InactiveHephaestus);
            worker2.setGodCard(GodCards.InactiveHephaestus);
            workerNormalRepository.save(worker1);
            workerNormalRepository.save(worker2);
        }
        if(godCard.equals("Hermes")) {
            worker1.setGodCard(GodCards.Hermes);
            worker2.setGodCard(GodCards.Hermes);
            workerNormalRepository.save(worker1);
            workerNormalRepository.save(worker2);
        }
        if(godCard.equals("InactiveHermes")) {
            worker1.setGodCard(GodCards.InactiveHermes);
            worker2.setGodCard(GodCards.InactiveHermes);
            if(currentGame.getGameStatus().equals(GameStatus.Move1)){
                currentGame.setGameStatus(GameStatus.Build1);
            }
            else if(currentGame.getGameStatus().equals(GameStatus.Move2)){
                currentGame.setGameStatus(GameStatus.Build2);
            }
            workerNormalRepository.save(worker1);
            workerNormalRepository.save(worker2);
        }
        if(godCard.equals("Prometheus")) {
            worker1.setGodCard(GodCards.Prometheus);
            worker2.setGodCard(GodCards.Prometheus);
            if(currentGame.getGameStatus().equals(GameStatus.Move1)){
                currentGame.setGameStatus(GameStatus.Build1);
            }
            else if(currentGame.getGameStatus().equals(GameStatus.Move2)){
                currentGame.setGameStatus(GameStatus.Build2);
            }
            workerNormalRepository.save(worker1);
            workerNormalRepository.save(worker2);
            gameRepository.save(currentGame);
        }
        if(godCard.equals("InactivePrometheus")) {
            worker1.setGodCard(GodCards.InactivePrometheus);
            worker2.setGodCard(GodCards.InactivePrometheus);
            workerNormalRepository.save(worker1);
            workerNormalRepository.save(worker2);
        }
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    public ResponseEntity<String> surrender(Long gameId, long playerId){

        Game currentGame = gameRepository.getById(gameId);
        if(currentGame.getPlayer1().getId() == playerId){
            currentGame.setGameStatus(GameStatus.Winner2);
        }
        else{
            currentGame.setGameStatus(GameStatus.Winner1);
        }
        gameRepository.save(currentGame);

        return new ResponseEntity<String>(HttpStatus.OK);
    }
}