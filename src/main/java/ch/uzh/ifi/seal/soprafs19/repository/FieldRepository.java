package ch.uzh.ifi.seal.soprafs19.repository;

import ch.uzh.ifi.seal.soprafs19.entity.Field;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// import javax.persistence.Tuple;

@Repository("fieldRepository")
public interface FieldRepository extends CrudRepository<Field, Long> {
    public Field findByXCoordinate(long xCoordinate);
    public Field findByYCoordinate(long yCoordinate);
}
