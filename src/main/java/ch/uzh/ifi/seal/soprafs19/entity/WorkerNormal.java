package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.constant.GodCards;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class WorkerNormal{
    @Id
    @GeneratedValue
    private int workerId ;
    private long playerId;
    private int position = -1;
    private boolean isNext = false;
    private GodCards godCard;
    private boolean isWinner = false;
    private int oldPosition = -1;
    private int buildingPosition = -1;
    private int oldBuildingPosition = -1;


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

    public void setOldBuildingPosition(int oldBuildingPosition) {
        this.oldBuildingPosition = oldBuildingPosition;
    }

    public int getOldBuildingPosition() {
        return oldBuildingPosition;
    }

    public void setBuildingPosition(int buildingPosition) {
        this.buildingPosition = buildingPosition;
    }

    public GodCards getGodCard() { return godCard;
    }

    public void setGodCard(GodCards godCard) {
        this.godCard = godCard;
    }

}
