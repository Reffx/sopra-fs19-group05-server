package ch.uzh.ifi.seal.soprafs19.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Worker implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;  // make all the worker share one id

    @Column
    private long playerId;

    //  abstract method to be implemented
    public abstract void move(long fieldId);
    public abstract void build(long fieldId, int level);

    public Long getId() { return this.id; }
    public void setId(long id) {this.id = id;}

    public long getPlayerId() {return this.playerId;}
    public void setPlayerId(long playerId) {this.playerId = playerId; }


    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Worker)) {
            return false;
        }
        Worker worker = (Worker) o;
        return this.getId().equals(worker.getId());
    }

}
