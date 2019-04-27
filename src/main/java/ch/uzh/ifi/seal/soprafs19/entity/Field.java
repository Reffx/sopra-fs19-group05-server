package ch.uzh.ifi.seal.soprafs19.entity;

import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import java.io.Serializable;
import javax.persistence.*;




@Embeddable
public class Field implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column
    private int fieldNum;

    @Column
    private int xCoordinate;
    @Column
    private int yCoordinate;

    @Column
    private int height;


    @Column // as a type worker, if no worker then null, else whole worker object //
    private boolean occupation;
    @Column
    private boolean reachedMaxHeight;


    public int getHeight() {return this.height;}
    public void setHeight(int height) {this.height = height;}

    public boolean getOccupation() {return this.occupation;}
    public void setOccupation(boolean occupation) {this.occupation = occupation;}

    public boolean getReachedMaxHeight() {return this.reachedMaxHeight;}
    public void setReachedMaxHeight(boolean reachedMaxHeight) {this.reachedMaxHeight = reachedMaxHeight;}

    public int getX_coordinate() {return this.xCoordinate;}
    public void setX_coordinate(int x_coordinate) {this.xCoordinate = x_coordinate;}

    public int getY_coordinate() {return this.yCoordinate;}
    public void setY_coordinate(int y_coordinate) {this.yCoordinate = y_coordinate;}



    public int getFieldNum() {return this.fieldNum;}
    public void setFieldNum(int num) {this.fieldNum = num;}



}