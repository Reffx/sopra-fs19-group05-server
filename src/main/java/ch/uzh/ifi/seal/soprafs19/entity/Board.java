package ch.uzh.ifi.seal.soprafs19.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;


@Entity
public class Board implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id; //equal to gameId //

    @ElementCollection
    private List<Field> allFields;


    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public List<Field> getAllFields() {return this.allFields;}
    public void setAllFields(List<Field> allFields) {this.allFields = allFields;}


}