package ch.uzh.ifi.seal.soprafs19.entity;


import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.AssertFalse;


@Entity
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id; //equal to gameId

    @OneToMany
    private List<Board> states;

    @Column
    private GameMode gameMode;

    //  setters and getters
    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public List<Board> getStates() {return states;}
    public void setStates(List<Board> states) { this.states = states; }

    public GameMode getGameMode() { return gameMode;}
    public void setGameMode(GameMode gameMode) { this.gameMode = gameMode;}


}