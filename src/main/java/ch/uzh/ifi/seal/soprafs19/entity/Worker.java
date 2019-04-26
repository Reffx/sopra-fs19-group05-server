package ch.uzh.ifi.seal.soprafs19.entity;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@MappedSuperclass
public class Worker implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;  // worker1 or worker2
    private long playerId;
    private int fieldNum;

    //  abstract method to be implemented
    //JuWe: changed params --> prior was fieldId, now its xCoordinate, yCoordinate
//    public abstract void move(long xCoordinate, long yCoordinate);
//    public abstract void build(long xCoordinate, long yCoordinate, int level);

    public int getId() { return this.id; }
    public void setId(int id) {this.id = id;}

    public long getPlayerId() {return this.playerId;}
    public void setPlayerId(long playerId) {this.playerId = playerId; }

    public void setFieldNum(int num){this.fieldNum = num;}
    public int getFieldNum(){return this.fieldNum;}


    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Worker)) {
            return false;
        }
        Worker worker = (Worker) o;
        return this.getId() == (worker.getId());
    }
    
//    //  define a class for embeddedId
//    @Embeddable
//    public class WorkerKey implements Serializable {
//
//        @Column(nullable = false)
//        private int id;
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public int getId() {
//            return id;
//        }
//    }

}
