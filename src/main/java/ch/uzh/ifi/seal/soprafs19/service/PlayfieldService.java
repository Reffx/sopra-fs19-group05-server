package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.entity.Game;
import ch.uzh.ifi.seal.soprafs19.entity.Field;
import ch.uzh.ifi.seal.soprafs19.entity.Playfield;
import ch.uzh.ifi.seal.soprafs19.repository.FieldRepository;
import ch.uzh.ifi.seal.soprafs19.repository.PlayfieldRepository;
import org.aspectj.apache.bcel.util.Play;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlayfieldService {
    private final PlayfieldRepository playfieldRepository;

    @Autowired
    public PlayfieldService(PlayfieldRepository playfieldRepository, FieldRepository fieldRepository) {

        this.playfieldRepository = playfieldRepository;
    }

    public Playfield initPlayfield(FieldService fieldService, Game game){
        Playfield playfield = new Playfield();
        playfield.setPlayfieldId(game.getId()); //
        Field initField;

        for (long x = 0; x < 4; x++){
            for (long y = 0; y < 4; y++){
                initField = fieldService.createField(x,y);
                playfield.allFields.add(initField);
            }
        }
        System.out.print(playfield);
        return playfield;
    }
}
