package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.constant.Color;
import org.hibernate.annotations.Target;
import org.hibernate.sql.Insert;

import java.io.Serializable;
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
   /* @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "workerId_1")),
            @AttributeOverride(name = "playerId", column = @Column(name = "playerId_1")),
            @AttributeOverride(name = "position", column = @Column(name = "position_1"))
    }) */
    //TODO: change @ value to one to one and
    @OneToOne(targetEntity = WorkerNormal.class, cascade = CascadeType.ALL)
    private WorkerNormal worker1;

   /* @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "workerId_2")),
            @AttributeOverride(name = "playerId", column = @Column(name = "playerId_2")),
            @AttributeOverride(name = "position", column = @Column(name = "position_2"))
    }) */
    @OneToOne(targetEntity = WorkerNormal.class, cascade = CascadeType.MERGE)
    private WorkerNormal worker2;

    /*//  constructor1
    public Player(Worker worker) {
        this.worker1 = worker;
        this.worker2 = worker;
    } */

      //constructor2 : set default as normal mode
    public Player() {
        /*
        this.worker1 = new WorkerNormal();
        //JUWE: set initial id value for worker 1 to 1
        this.worker1.setId(1);
        System.out.println(worker1.getId());
        this.worker2 = new WorkerNormal();
        //JUWE: set initial id value for worker 2 to 2
        this.worker2.setId(2);
        System.out.println(worker2.getId());*/
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