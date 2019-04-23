package ch.uzh.ifi.seal.soprafs19.entity;


import java.io.Serializable;
import java.util.List;
import javax.persistence.*;


@Entity
public class Playfield implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private long playFieldId; //equal to gameId //
/*
    @Column
    private List<Field> allFields;

    @Column
    private Tuple worker1;

    @Column
    private Tuple worker2;

    @Column
    private Tuple worker3;

    @Column
    private Tuple worker4;


    public long getPlayfieldId() {return playFieldId;}
    public void setPlayfieldId(long playFieldId) {this.playFieldId = playFieldId;} */

}