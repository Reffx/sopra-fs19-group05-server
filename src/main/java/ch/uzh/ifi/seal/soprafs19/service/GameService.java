package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.Color;
import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.Player;
import ch.uzh.ifi.seal.soprafs19.entity.WorkerNormal;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs19.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.repository.WorkerNormalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerService playerService;
    private final WorkerNormalRepository workerNormalRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public GameService(GameRepository gameRepository, PlayerService playerService, WorkerNormalRepository workerNormalRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.playerService = playerService;
        this.workerNormalRepository = workerNormalRepository;
        this.playerRepository = playerRepository;
    }

    //public ResponseEntity<>
    //  find all the games
    public Iterable<Game> getGames() {
        return this.gameRepository.findAll();
    }

    //  find all the NORMAL/GOD mode game
    public ResponseEntity<Iterable<Game>> getModeGames(GameMode gameMode) {
        return new ResponseEntity<Iterable<Game>>(gameRepository.findByGameMode(gameMode), HttpStatus.FOUND);
    }

    //  find a game by id
    public ResponseEntity<Game> getGame(long gameId) {
        return new ResponseEntity<Game>(gameRepository.getById(gameId), HttpStatus.FOUND);
    }

    //  create a game
    public ResponseEntity<Game> createGame(Game game) {
        //added if statement to check if the same user wants to create multiple games
        if(playerRepository.findByUsername(game.getPlayer1().getUsername())!= null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Player player1 = game.getPlayer1();   //  set the player1 as the creator
        //  save the player1 to the playerRepository
        //  save first to get gameId
        //game.setPlayer1(player1);
        WorkerNormal worker1 = new WorkerNormal();
        System.out.println(worker1.getWorkerId());
        workerNormalRepository.save(worker1);
        WorkerNormal worker2 = new WorkerNormal();
        workerNormalRepository.save(worker2);
        System.out.println(worker2.getWorkerId());
       // worker1 = workerNormalRepository.findById(0).get();
        System.out.println(worker1.getWorkerId());
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

        return new ResponseEntity<Game>(game, HttpStatus.CREATED); //   response code:201, frontend fetch the gameId in the body
    }

    //  update a game, add player1 or player2
    public ResponseEntity<Game> joinLobby(Long userId, Long gameId) {
        Game game = gameRepository.getById(gameId);
        if(game.getSize()==2){
            return new ResponseEntity<Game>(game, HttpStatus.CONFLICT);
        }
        //  create new player
        Player player = new Player();
        player.setId(userId);

        WorkerNormal worker1 = new WorkerNormal();
        WorkerNormal worker2 = new WorkerNormal();
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
        //JuWe: 01.05.19 changed resonse type to game, since its easier to understand if a player2 is in the game object
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
                return new ResponseEntity<String>(HttpStatus.CONFLICT);
            }
            else{
                player1.setColor(color);
                playerService.savePlayer(player1);
                return new ResponseEntity<String>(color.toString(), HttpStatus.OK);
            }
        }
        else if (player2 == playerX && player1 != null){
            if(player1.getColor() == color){
                return new ResponseEntity<String>(HttpStatus.CONFLICT);
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
        Player player = playerService.getPlayer(playerId);
        boolean status = player.getStatus();
        player.setStatus(!status);  //  negation of the original status

        playerService.savePlayer(player);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    //  set beginner JUWE: removed responseEntity because I had problems to call setBeginner in the WorkerService
    public Long setBeginner(Long gameId) {
        //  get playerId
        Long player1Id = gameRepository.getById(gameId).getPlayer1().getId();
        Long player2Id = gameRepository.getById(gameId).getPlayer2().getId();

        //  randomly select a player
        Random random = new Random();

        int id_rand = random.nextInt(2);
        if (id_rand == 0) {
            return player1Id;
        }
        return player2Id;
    }


    //  delete a game. when player1 exit
    public ResponseEntity<String> deleteGame(Long gameId) {
        gameRepository.deleteById(gameId);
        return new ResponseEntity<String>(HttpStatus.OK); // response code: 204
    }
}