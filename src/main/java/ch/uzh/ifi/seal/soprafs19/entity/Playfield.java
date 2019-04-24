package ch.uzh.ifi.seal.soprafs19.entity;


import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;


@Entity
public class Playfield implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private long playFieldId; //equal to gameId //

    @OneToMany
    public List<Field> allFields;


    public long getPlayfieldId() {return playFieldId;}
    public void setPlayfieldId(long playFieldId) {this.playFieldId = playFieldId;}

}