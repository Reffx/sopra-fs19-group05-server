package ch.uzh.ifi.seal.soprafs19.entity;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable
public class WorkerNormal extends Worker{

    public void moveTo(Field dest_field) {
        //  to implement, check coordnate and isOccupied
    }

    public void build(long gameId, int dest) {

    }
}
