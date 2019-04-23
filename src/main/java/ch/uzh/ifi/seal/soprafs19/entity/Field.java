package ch.uzh.ifi.seal.soprafs19.entity;

import java.io.Serializable;
import javax.persistence.*;


@Entity
public class Field implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Long xCoordinate;
    private Long yCoordinate;

    @Column
    private int height;

    @Column
    private boolean occupation;

    @Column
    private boolean reachedMaxHeight;



    public int getHeight() {return this.height;}
    public void setHeight(int height) {this.height = height;}

    public boolean getOccupation() {return this.occupation;}
    public void setOccupation(boolean occupation) {this.occupation = occupation;}

    public boolean getReachedMaxHeight() {return this.reachedMaxHeight;}
    public void setReachedMaxHeight(boolean reachedMaxHeight) {this.reachedMaxHeight = reachedMaxHeight;}

    public Long getX_coordinate() {return this.xCoordinate;}
    public void setX_coordinate(Long x_coordinate) {this.xCoordinate = x_coordinate;}

    public Long getY_coordinate() {return this.yCoordinate;}
    public void setY_coordinate(Long y_coordinate) {this.yCoordinate = y_coordinate;}



    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Field)) {
            return false;
        }
        Field field = (Field) o;
        return this.getX_coordinate().equals(field.getX_coordinate()) && this.getY_coordinate().equals(field.getY_coordinate());
    }

}