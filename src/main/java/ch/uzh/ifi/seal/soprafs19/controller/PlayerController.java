package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.Player;
import ch.uzh.ifi.seal.soprafs19.service.PlayerService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class PlayerController {

    private final PlayerService playerService;

    PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }


    @PostMapping("/players")
    ResponseEntity<Player> createPlayer(@RequestBody Player player){
        return playerService.createPlayer(player);
    }

    @GetMapping("/players")
    Iterable<Player> all() {
        return playerService.allPlayers();
    }

}

