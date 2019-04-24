package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.constant.Color;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    private Long id;

    @Column
    private Long gameId;

    @Column
    private Color color;

    @Column
    private boolean status;

    @Column
    private String username;

//    //  add worker1 and worker2 for player
//    @OneToMany
//    private Worker worker1;
//
//    @OneToMany
//    private Worker worker2;
//
//    //  constructor1
//    public Player(Worker worker) {
//        this.worker1 = worker;
//        this.worker2 = worker;
//    }
//
//    //  constructor2 : set default as normal mode
//    public Player() {
//        this.worker1 = new Worker0();
//        this.worker2 = new Worker0();
//    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }
    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    public Color getColor() {
        return this.color;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    public boolean getStatus() {
        return this.status;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return this.username;
    }

//    public void setWorker1(Worker worker) {
//        this.worker1 = worker;
//    }
//    public void setWorker2(Worker worker) {
//        this.worker2 = worker;
//    }
//
//    public Worker getWorker1() {
//        return worker1;
//    }
//    public Worker getWorker2() {
//        return worker2;
//    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Player)) {
            return false;
        }
        Player player = (Player) o;
        return this.getId().equals(player.getId());
    }

}