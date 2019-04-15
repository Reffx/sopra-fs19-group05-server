package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.constant.GameMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

//    @Column(nullable = false, unique = true)
//    private List<Long> players = new ArrayList<>(); // set default as empty list

//    @Column(nullable = false)
//    private Long creator;       // userId, so can build the body to Game object, set userId and playerId the same

//    @Column(nullable = false)
//    private String creator;

    @Column(nullable = false)
    private Long player1;       // playerId = userId

    @Column(nullable = true)
    private Long player2;

    @Column(nullable = false)
    private GameMode gameMode;

    @Column
    private String creationTime;

    @Column(nullable = false)
    private boolean isPlaying = false; // set default value as false

    //  TO DO: add gameBoard attribute

    public void setPlayer1(Long player1Id) {
        this.player1 = player1Id;
    }
    public Long getPlayer1() {
        return this.player1;
    }

    public void setPlayer2(Long player1Id) {
        this.player2 = player1Id;
    }
    public Long getPlayer2() {
        return this.player2;
    }
    public Long[] getPlayers() {
        Long[] players = new Long[2];
        players[0] = player1;
        players[1] = player2;
        return players;
    }

//    public  void setCreator(String userName) {
//        this.creator = userName;
//    }
//    public String getCreator() {
//        return this.creator;
//    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }
    public String getCreationTime() {
        return creationTime;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }
    public boolean getIsPlaying() {
        return isPlaying;
    }

    public void setGameMode(GameMode gameMode) {this.gameMode = gameMode;}
    public GameMode getGameMode() {return this.gameMode;}

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Game)) {
            return false;
        }
        Game game = (Game) o;
        return this.getId().equals(game.getId());
    }

}