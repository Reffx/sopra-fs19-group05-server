package ch.uzh.ifi.seal.soprafs19.entity;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Worker {

    @Id
    @GeneratedValue
    private static long id;  // make all the worker share one id

    @Column
    private long playerId;

    //  abstract method to be implemented
    public abstract void move(long fieldId);
    public abstract void build(long fieldId, int level);

    public long getId() { return this.id; }
    public void setId(long id) {this.id = id;}

    public long getPlayerId() {return this.playerId;}
    public void setPlayerId(long playerId) {this.playerId = playerId; }

}
