package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.Player;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.PlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/players/{id}")
    ResponseEntity getPlayer(@PathVariable Long id, @RequestParam("fields") String fields) {

        // TODO: get player by id

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/players/{id}")
    ResponseEntity updatePlayer(@PathVariable Long id) {

        // TODO: update player by id

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

