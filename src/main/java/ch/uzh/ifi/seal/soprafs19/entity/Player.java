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


    @OneToOne(targetEntity = WorkerNormal.class, cascade = CascadeType.ALL)
    private WorkerNormal worker1;

    @OneToOne(targetEntity = WorkerNormal.class, cascade = CascadeType.MERGE)
    private WorkerNormal worker2;


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

    public void setWorker1(WorkerNormal worker) {
        this.worker1 = worker;
    }
    public void setWorker2(WorkerNormal worker) {
        this.worker2 = worker;
    }

    public WorkerNormal getWorker1() {
        return worker1;
    }
    public WorkerNormal getWorker2() {
        return worker2;
    }

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