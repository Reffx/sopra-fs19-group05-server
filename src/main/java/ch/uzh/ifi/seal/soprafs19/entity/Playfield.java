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

    //  unique by onetoone
    @OneToOne(cascade = CascadeType.ALL)
    private Field field1;

    //  unique by onetoone
    @OneToOne(cascade = CascadeType.ALL)
    private Field field2;

    @OneToOne(cascade = CascadeType.ALL)
    private Field field3;

    //  unique by onetoone
    @OneToOne(cascade = CascadeType.ALL)
    private Field field4;

    @OneToOne(cascade = CascadeType.ALL)
    private Field field5;

    //  unique by onetoone
    @OneToOne(cascade = CascadeType.ALL)
    private Field field6;

    @OneToOne(cascade = CascadeType.ALL)
    private Field field7;

    //  unique by onetoone
    @OneToOne(cascade = CascadeType.ALL)
    private Field field8;

    @OneToOne(cascade = CascadeType.ALL)
    private Field field9;

    //  unique by onetoone
    @OneToOne(cascade = CascadeType.ALL)
    private Field field10;

    @OneToOne(cascade = CascadeType.ALL)
    private Field field11;

    //  unique by onetoone
    @OneToOne(cascade = CascadeType.ALL)
    private Field field12;

    @OneToOne(cascade = CascadeType.ALL)
    private Field field13;

    //  unique by onetoone
    @OneToOne(cascade = CascadeType.ALL)
    private Field field14;

    @OneToOne(cascade = CascadeType.ALL)
    private Field field15;

    //  unique by onetoone
    @OneToOne(cascade = CascadeType.ALL)
    private Field field16;

    @OneToOne(cascade = CascadeType.ALL)
    private Field field17;

    //  unique by onetoone
    @OneToOne(cascade = CascadeType.ALL)
    private Field field18;

    @OneToOne(cascade = CascadeType.ALL)
    private Field field19;

    //  unique by onetoone
    @OneToOne(cascade = CascadeType.ALL)
    private Field field20;

    @OneToOne(cascade = CascadeType.ALL)
    private Field field21;

    //  unique by onetoone
    @OneToOne(cascade = CascadeType.ALL)
    private Field field22;

    @OneToOne(cascade = CascadeType.ALL)
    private Field field23;

    //  unique by onetoone
    @OneToOne(cascade = CascadeType.ALL)
    private Field field24;

    @OneToOne(cascade = CascadeType.ALL)
    private Field field25;


    public long getPlayfieldId() {return playFieldId;}
    public void setPlayfieldId(long playFieldId) {this.playFieldId = playFieldId;}

    public void setField1(Field field1) {
        this.field1 = field1;
    }
    public Field getField1() {
        return this.field1;
    }

    public void setField2(Field field2) {
        this.field2 = field2;
    }
    public Field getField2() {
        return this.field2;
    }
    public void setField3(Field field3) {
        this.field3 = field3;
    }
    public Field getField3() {
        return this.field3;
    }
    public void setField4(Field field4) {
        this.field4 = field4;
    }
    public Field getField4() {
        return this.field4;
    }
    public void setField5(Field field5) {
        this.field5 = field5;
    }
    public Field getField5() {
        return this.field5;
    }
    public void setField6(Field field6) {
        this.field6 = field6;
    }
    public Field getField6() {
        return this.field6;
    }
    public void setField7(Field field7) {
        this.field7 = field7;
    }
    public Field getField7() {
        return this.field7;
    }
    public void setField8(Field field8) {
        this.field8 = field8;
    }
    public Field getField8() {
        return this.field8;
    }
    public void setField9(Field field9) {
        this.field9 = field9;
    }
    public Field getField9() {
        return this.field9;
    }
    public void setField10(Field field10) { this.field10 = field10; }
    public Field getField10() {
        return this.field10;
    }
    public void setField11(Field field11) {
        this.field11 = field11;
    }
    public Field getField11() {
        return this.field11;
    }
    public void setField12(Field field12) {
        this.field12 = field12;
    }
    public Field getField12() {
        return this.field12;
    }
    public void setField13(Field field13) {
        this.field13 = field13;
    }
    public Field getField13() {
        return this.field13;
    }
    public void setField14(Field field14) {
        this.field14 = field14;
    }
    public Field getField14() {
        return this.field14;
    }
    public void setField15(Field field15) {
        this.field15 = field15;
    }
    public Field getField15() {
        return this.field15;
    }
    public void setField16(Field field16) {
        this.field16 = field16;
    }
    public Field getField16() {
        return this.field16;
    }
    public void setField17(Field field17) {
        this.field17 = field17;
    }
    public Field getField17() {
        return this.field17;
    }
    public void setField18(Field field18) {
        this.field18 = field18;
    }
    public Field getField18() {
        return this.field18;
    }
    public void setField19(Field field19) {
        this.field19 = field19;
    }
    public Field getField19() {
        return this.field19;
    }
    public void setField20(Field field20) {
        this.field20 = field20;
    }
    public Field getField20() {
        return this.field20;
    }
    public void setField21(Field field21) {
        this.field21 = field21;
    }
    public Field getField21() {
        return this.field21;
    }
    public void setField22(Field field22) {
        this.field22 = field22;
    }
    public Field getField22() {
        return this.field22;
    }
    public void setField23(Field field23) {
        this.field23 = field23;
    }
    public Field getField23() {
        return this.field23;
    }
    public void setField24(Field field24) {
        this.field24 = field24;
    }
    public Field getField24() {
        return this.field24;
    }
    public void setField25(Field field25) {
        this.field25 = field25;
    }
    public Field getField25() {
        return this.field25;
    }




}