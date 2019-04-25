package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.entity.Game;
import org.aspectj.apache.bcel.util.Play;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ch.uzh.ifi.seal.soprafs19.entity.Field;
import ch.uzh.ifi.seal.soprafs19.entity.Playfield;
import ch.uzh.ifi.seal.soprafs19.service.FieldService;
import ch.uzh.ifi.seal.soprafs19.repository.FieldRepository;
import ch.uzh.ifi.seal.soprafs19.repository.PlayfieldRepository;

@Service
@Transactional
public class PlayfieldService {
    private final PlayfieldRepository playfieldRepository;
    private final FieldRepository fieldRepository;
    private final FieldService fieldService;

    @Autowired
    public PlayfieldService(PlayfieldRepository playfieldRepository, FieldRepository fieldRepository, FieldService fieldService) {

        this.playfieldRepository = playfieldRepository;
        this.fieldRepository = fieldRepository;
        this.fieldService = fieldService;
    }

    public Playfield initPlayfield(long gameId){

       Playfield newPlayfield = new Playfield();
       Field field1 = fieldService.createField(0,0);
       Field field2 = fieldService.createField(0,1);
       Field field3 = fieldService.createField(0,2);
       Field field4 = fieldService.createField(0,3);
        Field field5 = fieldService.createField(0,4);
        Field field6 = fieldService.createField(1,0);
        Field field7 = fieldService.createField(1,1);
        Field field8 = fieldService.createField(1,2);
        Field field9 = fieldService.createField(1,3);
        Field field10 = fieldService.createField(1,4);
        Field field11 = fieldService.createField(2,0);
        Field field12 = fieldService.createField(2,1);
        Field field13 = fieldService.createField(2,2);
        Field field14 = fieldService.createField(2,3);
        Field field15 = fieldService.createField(2,4);
        Field field16 = fieldService.createField(3,0);
        Field field17 = fieldService.createField(3,1);
        Field field18 = fieldService.createField(3,2);
        Field field19 = fieldService.createField(3,3);
        Field field20 = fieldService.createField(3,4);
        Field field21= fieldService.createField(4,0);
        Field field22 = fieldService.createField(4,1);
        Field field23 = fieldService.createField(4,2);
        Field field24 = fieldService.createField(4,3);
        Field field25 = fieldService.createField(4,4);
        fieldRepository.save(field1);
        fieldRepository.save(field2);
        fieldRepository.save(field3);
        fieldRepository.save(field4);
        fieldRepository.save(field5);
        fieldRepository.save(field6);
        fieldRepository.save(field7);
        fieldRepository.save(field8);
        fieldRepository.save(field9);
        fieldRepository.save(field10);
        fieldRepository.save(field11);
        fieldRepository.save(field12);
        fieldRepository.save(field13);
        fieldRepository.save(field14);
        fieldRepository.save(field15);
        fieldRepository.save(field16);
        fieldRepository.save(field17);
        fieldRepository.save(field18);
        fieldRepository.save(field19);
        fieldRepository.save(field20);
        fieldRepository.save(field21);
        fieldRepository.save(field22);
        fieldRepository.save(field23);
        fieldRepository.save(field24);
        fieldRepository.save(field25);

        newPlayfield.setField1(field1);
        newPlayfield.setField2(field2);
        newPlayfield.setField3(field3);
        newPlayfield.setField4(field4);
        newPlayfield.setField5(field5);
        newPlayfield.setField6(field6);
        newPlayfield.setField7(field7);
        newPlayfield.setField8(field8);
        newPlayfield.setField9(field9);
        newPlayfield.setField10(field10);
        newPlayfield.setField11(field11);
        newPlayfield.setField12(field12);
        newPlayfield.setField13(field13);
        newPlayfield.setField14(field14);
        newPlayfield.setField15(field15);
        newPlayfield.setField16(field16);
        newPlayfield.setField17(field17);
        newPlayfield.setField18(field18);
        newPlayfield.setField19(field19);
        newPlayfield.setField20(field20);
        newPlayfield.setField21(field21);
        newPlayfield.setField22(field22);
        newPlayfield.setField23(field23);
        newPlayfield.setField24(field24);
        newPlayfield.setField25(field25);

        playfieldRepository.save(newPlayfield);
        return newPlayfield;
    }

    public Playfield updatePlayfield (Playfield newPlayfield){
        Playfield tempPlayfield = playfieldRepository.findByPlayFieldId(newPlayfield.getPlayfieldId());
        playfieldRepository.save(tempPlayfield);
        return newPlayfield;
    }

    public ResponseEntity<Playfield> getPlayfield(long playfieldId) {
        return new ResponseEntity<Playfield>(playfieldRepository.findByPlayFieldId(playfieldId), HttpStatus.FOUND);
    }

}
