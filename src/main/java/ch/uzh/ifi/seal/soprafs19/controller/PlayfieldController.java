package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.Playfield;
import ch.uzh.ifi.seal.soprafs19.service.PlayfieldService;
import ch.uzh.ifi.seal.soprafs19.entity.Game;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class PlayfieldController {

    private final PlayfieldService playfieldService;

    private PlayfieldController(PlayfieldService playfieldService) {
        this.playfieldService = playfieldService;
    }

    @PostMapping("/games/{gameId}/playfield")
    Playfield newPlayfield(@PathVariable Long gameId){
        return playfieldService.initPlayfield(gameId);
    }

    @CrossOrigin
    @PutMapping("playfield/{playfieldId}")
    Playfield updatePlayfield(@RequestBody Playfield newPlayfield){ return this.playfieldService.updatePlayfield(newPlayfield);}
}