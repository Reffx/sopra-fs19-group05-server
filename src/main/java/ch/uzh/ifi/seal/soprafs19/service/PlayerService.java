package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.entity.Player;
import ch.uzh.ifi.seal.soprafs19.repository.PlayerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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


    //  all players
    public Iterable<Player> allPlayers() {
        return playerRepository.findAll();
    }

    //  get player by id
    public Player getPlayer(Long playerId) {
        return playerRepository.getById(playerId);
    }

    //  save player
    void savePlayer(Player player) {
        playerRepository.save(player);
    }

    // delete a player by id
    public ResponseEntity<String> deletebyId(Long playerId) {
        playerRepository.deleteById(playerId);
        return new ResponseEntity<String> (HttpStatus.OK);
    }

    //get username
    public String getUsername(Long playerId) {return userService.getUser(playerId).getUsername();}

}