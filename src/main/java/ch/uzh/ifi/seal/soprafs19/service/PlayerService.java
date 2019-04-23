package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.Color;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.Player;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
public class PlayerService {

    public final PlayerRepository playerRepository;
    private final UserService userService;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, UserService userService) {
        this.playerRepository = playerRepository;
        this.userService = userService;
    }

    //  create a new player
    public ResponseEntity<Player> createPlayer(Player newPlayer) {

        //  set the username here and save it to the player
        Long userId = newPlayer.getId();
        User user = userService.getUser(userId);
        String username = user.getUsername();
        newPlayer.setUsername(username);

        playerRepository.save(newPlayer);
        return new ResponseEntity<Player>(newPlayer, HttpStatus.CREATED);
    }

    //  all players
    public Iterable<Player> allPlayers() {
        return playerRepository.findAll();
    }

    //  get player by id
    Player getPlayer(Long playerId) {
        return playerRepository.getById(playerId);
    }

    //  save player
    void savePlayer(Player player) {
        playerRepository.save(player);
    }

}