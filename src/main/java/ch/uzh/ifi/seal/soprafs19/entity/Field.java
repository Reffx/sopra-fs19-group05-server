package ch.uzh.ifi.seal.soprafs19.entity;


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



    @OneToOne(targetEntity = WorkerNormal.class, cascade= CascadeType.MERGE)
    private WorkerNormal occupier;


    public int getHeight() {return this.height;}
    public void setHeight(int height) {this.height = height;}

    public WorkerNormal getOccupier() {return this.occupier;}
    //JUWE: updated setOccupier to Worker parameter, prior boolean, better set
    public void setOccupier(WorkerNormal occupier) {this.occupier = occupier;}


    public int getX_coordinate() {return this.xCoordinate;}
    public void setX_coordinate(int x_coordinate) {this.xCoordinate = x_coordinate;}

    public int getY_coordinate() {return this.yCoordinate;}
    public void setY_coordinate(int y_coordinate) {this.yCoordinate = y_coordinate;}


    public int getFieldNum() {return this.fieldNum;}
    public void setFieldNum(int num) {this.fieldNum = num;}



}