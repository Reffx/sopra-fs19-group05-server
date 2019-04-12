package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.constant.GameMode;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private List<Player> players;

    @Column(nullable = false)
    private GameMode gameMode;

    @Column(nullable = true)
    private String creationTime;

    public void setPlayers(List<Player> players) {
        this.players = players;
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