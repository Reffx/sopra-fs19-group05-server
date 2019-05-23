package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.constant.Color;
import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class GameController {
    private final GameService gameService;

    GameController(GameService gameService) {
        this.gameService = gameService;
    }

    //  create a game
    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    Game createGame(@RequestBody Game game) {
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
        return gameService.getGame(gameId);
    }

    //  add player2
    @PutMapping("/games/{gameId}/player")
    @CrossOrigin
    ResponseEntity<Game> joinLobby(@RequestBody Long userId, @PathVariable Long gameId) {
        return gameService.joinLobby(userId, gameId);
    }

    //  ser player color
    @PutMapping("/games/{gameId}/{playerId}/color")
    @CrossOrigin
    ResponseEntity<String> setColor(@PathVariable(name = "gameId") Long gameId, @PathVariable(name = "playerId") Long playerId, @RequestBody Color color) {
        return gameService.setColor(gameId, playerId, color);
    }

    //  set player status
    @PutMapping("/games/{gameId}/{playerId}/status")
    @CrossOrigin
    ResponseEntity<String> setStatus(@PathVariable(name = "gameId") Long gameId, @PathVariable(name = "playerId") Long playerId) {
        return gameService.setStatus(gameId, playerId);
    }

    //  set beginner
    @GetMapping("/games/{gameId}/beginner")
    public Long setBeginner(@PathVariable Long gameId) {
        return gameService.setBeginner(gameId);
    }

    //  remove player
    @PutMapping("/games/{gameId}/{playerId}")
    @CrossOrigin
    ResponseEntity<String> leaveLobby(@PathVariable(name = "gameId") Long gameId, @PathVariable(name = "playerId") Long playerId) {
        return gameService.leaveLobby(gameId, playerId);
    }

//    //  delete a game
//    @PutMapping("/games/{gameId}")
//    ResponseEntity<String> deleteGame(@PathVariable Long gameId) {
//        return gameService.deleteGame(gameId);
//    }

    @PutMapping("/games/{gameId}/{playerId}/GodCard")
    ResponseEntity<String> assignGodCard(@RequestBody String godCard, @PathVariable(name = "playerId") long playerId){
        return gameService.assignGodCard(godCard, playerId);
    }

    //surrender
    @PutMapping("/games/{gameId}/{playerId}/surrender")
    @CrossOrigin
    ResponseEntity<String> surrender(@PathVariable(name = "gameId") long gameId, @PathVariable(name = "playerId") long playerId){
        return gameService.surrender(gameId, playerId);
    }
}
