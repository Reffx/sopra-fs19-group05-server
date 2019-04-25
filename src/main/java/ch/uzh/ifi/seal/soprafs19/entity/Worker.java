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

    //JuWe: added 2 columns for the WorkerRepo to save the x and y coordinate
    @Column
    private long xCoordinate;

    @Column
    private long yCoordinate;

    //  abstract method to be implemented
    //JuWe: changed params --> prior was fieldId, now its xCoordinate, yCoordinate
    public abstract void move(long xCoordinate, long yCoordinate);
    public abstract void build(long xCoordinate, long yCoordinate, int level);

    public Long getId() { return this.id; }
    public void setId(long id) {this.id = id;}

    public long getPlayerId() {return this.playerId;}
    public void setPlayerId(long playerId) {this.playerId = playerId; }

    //JuWe: added setter and getter functions for both coordinates
    public void setXCoordinate(long xCoordinate){this.xCoordinate=xCoordinate;}
    public long getXCoordinate(){return this.xCoordinate;}

    public void setYCoordinate(long yCoordinate){this.yCoordinate=yCoordinate;}
    public long getYCoordinate(){return this.yCoordinate;}


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
