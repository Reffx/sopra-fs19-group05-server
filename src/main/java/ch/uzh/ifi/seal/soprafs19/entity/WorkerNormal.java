package ch.uzh.ifi.seal.soprafs19.entity;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class WorkerNormal{
    @Id
    @GeneratedValue
    private int workerId ;  // worker1 or worker2 --> DA: better to just have workers 1 - 4, we can check which belongs to who with the attribute playerId //
    private long playerId;
    private int position;
    private boolean isNext = false;


    public boolean isWinner(){
        return true;
    };

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
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
