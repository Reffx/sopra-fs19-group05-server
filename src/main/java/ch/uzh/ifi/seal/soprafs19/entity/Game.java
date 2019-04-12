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

    @Column(nullable = false, unique = true)
    private List<Player> players = new ArrayList<>(); // set default as empty list

    @Column(nullable = false)
    private GameMode gameMode = GameMode.GOD;   // set default value as GOD

    @Column(nullable = true)
    private String creationTime;

    @Column(nullable = false)
    private boolean isPlaying = false; // set default value as false

    //  TO DO: add gameBoard attribute

    public void setPlayers(Player player) {
        this.players.add(player);
    }
    public List<Player> getPlayers() {
        return players;
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

    public void setIs_playing(boolean is_playing) {
        this.isPlaying = is_playing;
    }
    public boolean getIs_playing() {
        return isPlaying;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Game)) {
            return false;
        }
        Game Game = (Game) o;
        return this.getId().equals(Game.getId());
    }

}