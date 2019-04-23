package ch.uzh.ifi.seal.soprafs19.entity;

<<<<<<< HEAD
=======
import ch.uzh.ifi.seal.soprafs19.constant.GameMode;
import org.hibernate.annotations.Cascade;

>>>>>>> feature_Game_backend
import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

<<<<<<< HEAD
    @Column(nullable = false, unique = true)
    private Long gameId;

    @Column(nullable = false, unique = true)
    private Long playerId;

    @Column(nullable = false)

    public Long getId() {
        return id;
=======
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
>>>>>>> feature_Game_backend
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long userId) {
        this.playerId = playerId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

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
        Game Game = (Game) o;
        return this.getId().equals(Game.getId());
    }

}