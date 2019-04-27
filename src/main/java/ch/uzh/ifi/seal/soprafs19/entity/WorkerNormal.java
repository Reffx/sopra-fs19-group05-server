package ch.uzh.ifi.seal.soprafs19.entity;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable
public class WorkerNormal extends Worker{

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
}
