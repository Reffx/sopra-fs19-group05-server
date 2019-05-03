package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.constant.Color;
import org.hibernate.annotations.Target;
import org.hibernate.sql.Insert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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

    //  add worker1 and worker2 for player
//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "id", column = @Column(name = "workerId_1")),
//            @AttributeOverride(name = "playerId", column = @Column(name = "playerId_1")),
//            @AttributeOverride(name = "position", column = @Column(name = "position_1"))
//    })
//    @Target(WorkerNormal.class)
//    private Worker worker1;
//
//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "id", column = @Column(name = "workerId_2")),
//            @AttributeOverride(name = "playerId", column = @Column(name = "playerId_2")),
//            @AttributeOverride(name = "position", column = @Column(name = "position_2"))
//    })
//    @Target(WorkerNormal.class)
//    private Worker worker2;

    @ElementCollection
    private List<Worker> workers;

//    //  constructor1
//    public Player(Worker worker) {
//        this.worker1 = worker;
//        this.worker2 = worker;
//    }

      //constructor2 : set default as normal mode
    public Player() {
        Worker worker1 = new WorkerNormal();
        worker1.setId(1);
        Worker worker2 = new WorkerNormal();
        worker2.setId(2);

        List<Worker> workers = new ArrayList<>();
        workers.add(worker1);
        workers.add(worker2);
        this.workers = workers;
    }

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

    public void setWorker1(Worker worker) {
        this.workers.set(0, worker);
    }
    public void setWorker2(Worker worker) {
        this.workers.set(1, worker);
    }

    public Worker getWorker1() {
        return this.workers.get(0);
    }
    public Worker getWorker2() {
        return this.workers.get(1);
    }

    public List<Worker> gerWorkers() {
        return this.workers;
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