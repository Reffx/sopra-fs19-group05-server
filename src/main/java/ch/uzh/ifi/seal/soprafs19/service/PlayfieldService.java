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

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PlayfieldService {
    private final PlayfieldRepository playfieldRepository;
    private final FieldService fieldService;

    @Autowired
    public PlayfieldService(PlayfieldRepository playfieldRepository, FieldService fieldService) {

        this.playfieldRepository = playfieldRepository;
        this.fieldService = fieldService;
    }

    public Playfield initPlayfield(Long gameId) {

        Playfield playfield = new Playfield();
        playfield.setId(gameId);
        List<Field> allFields = new ArrayList<Field>();
        //  initialize the coordinates
        int num = 0;
        for( int i = 0; i <= 4; ++i) {
            for (int j = 0; j <= 4; ++j) {
                Field field = new Field();

                field.setFieldNum(num);
                field.setX_coordinate(i);
                field.setY_coordinate(j);
                ++num;
                allFields.add(field);
                fieldService.saveField(field);
            }
        }

        playfield.setAllFields(allFields);
        playfieldRepository.save(playfield);
        return playfield;
    }

    public Playfield updatePlayfield (Playfield newPlayfield){
        Playfield tempPlayfield = playfieldRepository.findById(newPlayfield.getId());
        playfieldRepository.save(tempPlayfield);
        return newPlayfield;
    }

    //JuWe: updated the getPlayfield function in order to have the functionailty to create a playfield or retrieve one from the repo
    public Playfield getPlayfield(long gameId) {
        if (playfieldRepository.findById(gameId) != null) {
            return (playfieldRepository.findById(gameId));
        }
        else{
            return (initPlayfield(gameId);
        }
    }

}
