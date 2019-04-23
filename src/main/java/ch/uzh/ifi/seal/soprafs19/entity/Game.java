package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import org.hibernate.annotations.Cascade;

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

    //  unique by onetoone
    @OneToOne(cascade = CascadeType.ALL)        //  to map the player database to the player database
    private Player player1;                    // playerId = userId

    //  unique by onetoone
    @OneToOne(cascade = CascadeType.ALL)
    private Player player2;

    @Column(nullable = false)
    private GameMode gameMode;

    @Column
    private int size = 1;

    @Column
    private String creationTime;

    @Column(nullable = false)
    private boolean isPlaying = false;

    //  TO DO: add gameBoard attribute

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }
    public Player getPlayer1() {
        return this.player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }
    public Player getPlayer2() {
        return this.player2;
    }

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

    public void setSize(int size) {
        this.size = size;
    }
    public int getSize() {
        return this.size;
    }


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