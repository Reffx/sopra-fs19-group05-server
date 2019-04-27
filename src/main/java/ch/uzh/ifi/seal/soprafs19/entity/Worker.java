package ch.uzh.ifi.seal.soprafs19.entity;

import org.springframework.http.ResponseEntity;

import javax.persistence.*;
import java.io.Serializable;


public abstract class Worker implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id ;  // worker1 or worker2
    private long playerId;
    private int position;

    //  abstract method to be implemented
    //  here is a inner move function, there are move and build function in workerService, return ResponseEntity
    public abstract void moveTo(Field dest_field);
    public abstract void build(long gameId, int dest);

    public int getId() { return this.id; }
    public void setId(int id) {this.id = id;}

    public long getPlayerId() {return this.playerId;}
    public void setPlayerId(long playerId) {this.playerId = playerId; }

    public void setPosition(int num){this.position = num;}
    public int getPosition(){return this.position;}


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
