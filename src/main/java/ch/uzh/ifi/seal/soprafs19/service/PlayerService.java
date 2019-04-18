package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.entity.Player;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlayerService {

    private final Logger log = LoggerFactory.getLogger(PlayerService.class);

    public final PlayerRepository playerRepository;


    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    //  create a new player
    public ResponseEntity<Player> createPlayer(Player newPlayer) {

        //  To Do: set the username here

        playerRepository.save(newPlayer);
        return new ResponseEntity<Player>(newPlayer, HttpStatus.CREATED);

    }

    //  set player color


  // public ResponseEntity<Player> createPlayer(Player newPlayer, User user) {
        //DA: I would set both username & id here, currently id is assigned in Gameservice
        //DA: In createGame, where player1 is created no id is assigned &
        //DA: In addPlayer2 a id is assigned
        //DA: I added the import of the user entity access id & username

   //     newPlayer.setUsername(user.getUsername());
   //     newPlayer.setId(user.getId());

   //     playerRepository.save(newPlayer);

   //     return new ResponseEntity<Player>(newPlayer, HttpStatus.CREATED);
}