package ch.uzh.ifi.seal.soprafs19.entity;

import java.io.Serializable;
import java.util.Objects;

public class Coordinates implements Serializable {
    private Long xCoordinate;
    private Long yCoordinate;

    public Long getX_coordinate() {return this.xCoordinate;}
    public void setX_coordinate(Long x_coordinate) {this.xCoordinate = x_coordinate;}

    public Long getY_coordinate() {return this.yCoordinate;}
    public void setY_coordinate(Long y_coordinate) {this.yCoordinate = y_coordinate;}

    @Override //redo with coordinates //
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Field)) {
            return false;
        }
        Field field = (Field) o;
        return this.getX_coordinate().equals(field.getX_coordinate()) && this.getY_coordinate().equals(field.getY_coordinate());
    }
    @Override
    public int hashCode(){
        return this.getX_coordinate().hashCode() & this.getY_coordinate().hashCode();
    }
}