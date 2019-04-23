package ch.uzh.ifi.seal.soprafs19.service;


import ch.uzh.ifi.seal.soprafs19.entity.Field;
import ch.uzh.ifi.seal.soprafs19.repository.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FieldService {
    private final FieldRepository fieldRepository;

    @Autowired
    public FieldService(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }


    public Field createField(Long x_coordinate, Long y_coordinate){
        Field field = new Field();
        field.setX_coordinate(x_coordinate);
        field.setY_coordinate(y_coordinate);
        fieldRepository.save(field);
        return field;
    }

}