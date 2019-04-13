package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.Game;
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

    @GetMapping("/games")
    Iterable<Game> all() {
        return gameService.getGames();  // for lobby in the frondend
    }

    @GetMapping("/games/{id}")
    ResponseEntity<Game> getGame(@PathVariable Long id) {
        //  TO DO: display individual lobby
    }

    @PutMapping("/games/{id}/players/player2")
    ResponseEntity<String> addPlayer(@RequestBody Long player2) {
        //  TO DO: add player2 to the game
    }


}
