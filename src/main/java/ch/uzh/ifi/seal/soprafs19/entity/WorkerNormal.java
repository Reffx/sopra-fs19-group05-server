package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.constant.GodCards;

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
    private int position = -1;
    private boolean isNext = false;
    private GodCards godCard;
    private boolean isWinner = false;
    private int oldPosition = -1;
    private int buildingPosition = -1;
    private int oldbuildingPosition = -1;


    public void setIsWinner(boolean isWinner) {
        this.isWinner = isWinner;
    }
    public boolean getIsWinner() {
        return this.isWinner;
    }

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
    public int getOldPosition() {
        return oldPosition;
    }

    public void setOldPosition(int oldPosition) {
        this.oldPosition = oldPosition;
    }

    public int getBuildingPosition() {
        return buildingPosition;
    }

    public void setOldbuildingPosition(int oldbuildingPosition) {
        this.oldbuildingPosition = oldbuildingPosition;
    }

    public int getOldbuildingPosition() {
        return oldbuildingPosition;
    }

    public void setBuildingPosition(int buildingPosition) {
        this.buildingPosition = buildingPosition;
    }

    public GodCards getGodCard() { return godCard;
    }

    public void setGodCard(GodCards godCard) {
        this.godCard = godCard;
    }

    public boolean isNext() {
        return isNext;
    }

    public void setNext(boolean next) {
        isNext = next;
    }
}
