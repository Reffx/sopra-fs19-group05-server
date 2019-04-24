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

    @Autowired
    public PlayfieldService(PlayfieldRepository playfieldRepository, FieldRepository fieldRepository) {

        this.playfieldRepository = playfieldRepository;
        this.fieldRepository = fieldRepository;

    }

    public Playfield initPlayfield(long playfieldId){

       Playfield newPlayfield = playfieldRepository.findByPlayFieldId(playfieldId);

        for (long x = 0; x <= 4; x++){
            for (long y = 0; y <= 4; y++){
                Field initField = new Field();
                initField.setX_coordinate(x);
                initField.setY_coordinate(y);
                fieldRepository.save(initField);
                newPlayfield.allFields.add(initField);
            }
        }
        playfieldRepository.save(newPlayfield);
        System.out.print(newPlayfield);
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
