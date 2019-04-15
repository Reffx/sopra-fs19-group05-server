package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.constant.GameStatus;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long gameId;

    // add attribute for player amount in lobby //
    @Column(nullable = false)
    private Integer lobbySize;

    @Column(nullable = false, unique = true)
    private String gameName;

    // Add variable game status, either isplaying or iswaiting //
    @Column(nullable = false)
    private GameStatus gameStatus;

    @Column(nullable = false)
    private String gameMode;

    // add methods to get/set gameStatus //
    public GameStatus  getGameStatus(){
        return gameStatus;
    }
    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    // add methods to get/set lobbysize //
    public Integer getLobbySize(){
        return lobbySize;
    }
    public void setLobbySize(Integer lobbySize){
        this.lobbySize = lobbySize;
    }

    // add methods to get/set gameMode //
    public String getGameMode(){
        return gameMode;
    }
    public void setGameMode(String gameMode){
        this.gameMode = gameMode;
    }
    public String getGameName(){
        return gameName;
    }
    public void setGameName(String gameName){
        this.gameName = gameName;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Game)) {
            return false;
        }
        Game Game = (Game) o;
        return this.getGameId().equals(Game.getGameId());
    }

}