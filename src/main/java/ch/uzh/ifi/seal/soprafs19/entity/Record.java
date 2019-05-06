package ch.uzh.ifi.seal.soprafs19.entity;


import ch.uzh.ifi.seal.soprafs19.repository.GameRepository;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;


@Entity
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id; //equal to gameId

    @OneToMany
    private List<Board> records;


    //  setters and getters
    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public List<Board> getRecords() {return records;}
    public void setRecords(List<Board> records) {
        this.records = records;
    }
}