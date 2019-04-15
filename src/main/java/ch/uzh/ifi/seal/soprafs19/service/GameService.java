package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;

import ch.uzh.ifi.seal.soprafs19.controller.DuplicateException;
import ch.uzh.ifi.seal.soprafs19.controller.NonExistentUserException;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }


    public Iterable<Game> getGames() {
        return this.gameRepository.findAll();
    }

    public Game createLobby(Game newLobby, User existingUser) {

        newLobby.setGameStatus(GameStatus.IS_WAITING);
        newLobby.setGameName(existingUser.getUsername() + existingUser.getId().toString());
        newLobby.setLobbySize(1);
        gameRepository.save(newLobby);
        log.debug("Created Information for Lobby: {}", newLobby);
        return newLobby;
    }

    //return lobby from GameRepository
    public Game getGame(long id) {
        Game tempGame = gameRepository.findByGameId(id);
        if (tempGame != null) {
            return tempGame;
        }
        else{
            return null;
        }

    }
}
