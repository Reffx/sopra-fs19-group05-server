package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class GameController {
    private final GameService gameService;

    GameController(GameService gameService) {
        this.gameService = gameService;
    }

    //  create a game
    @PostMapping("/games")
    ResponseEntity<Game> createGame(@RequestBody Game game) {
        return gameService.createGame(game);
    }

    //  find all NORMAl or GOD game
    @GetMapping("/games/mode/{gameMode}")
    ResponseEntity<Iterable<Game>> getModeGames(@PathVariable GameMode gameMode) {
        return gameService.getModeGames(gameMode);
    }

    //  find all the games
    @GetMapping("/games")
    Iterable<Game> all() {
        return gameService.getGames();  // for lobby in the frondend
    }


    //  find game by id
    @GetMapping("/games/{gameId}")
    @CrossOrigin
    ResponseEntity<Game> getGame(@PathVariable Long gameId) {
        //TEST
//        System.out.println(gameId);
        return gameService.getGame(gameId);
    }

    //  add player2
    @PutMapping("/games/{gameId}/player2")
    @CrossOrigin
    ResponseEntity<String> addPlayer2(@RequestBody Long userId, @PathVariable Long gameId) {

        return gameService.addPlayer2(userId, gameId);
    }



//    @PutMapping("/games/{id}/players/player2")
//    ResponseEntity<Void> addPlayer(@RequestBody Long player2) {
//        //  TO DO: add player2 to the game
//    }

}
