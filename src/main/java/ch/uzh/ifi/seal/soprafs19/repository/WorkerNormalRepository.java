package ch.uzh.ifi.seal.soprafs19.repository;

import ch.uzh.ifi.seal.soprafs19.entity.WorkerNormal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerNormalRepository extends CrudRepository<WorkerNormal, Integer> {
    WorkerNormal findById(int id);
}
