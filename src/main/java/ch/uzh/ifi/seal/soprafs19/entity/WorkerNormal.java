package ch.uzh.ifi.seal.soprafs19.entity;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class WorkerNormal{
    @Id
    @GeneratedValue
    private int id ;  // worker1 or worker2
    private long playerId;
    private int position;
    private boolean isNext = false;

    public boolean moveTo(Field depart, Field dest) {
        //  to implement, check coordnate and isOccupied
        int dest_x = dest.getX_coordinate();
        int dest_y = dest.getY_coordinate();

        int depart_x = depart.getX_coordinate();
        int depart_y = depart.getY_coordinate();

        if (dest.getOccupier() != null) {
            return false;
        }
        return true;

    }

    public void build(long gameId, int dest) {

    }

    public boolean isWinner(){
        return false;
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isNext() {
        return isNext;
    }

    public void setNext(boolean next) {
        isNext = next;
    }
}
