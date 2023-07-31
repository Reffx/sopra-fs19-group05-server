package ch.uzh.ifi.seal.soprafs19.repository;

import ch.uzh.ifi.seal.soprafs19.entity.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<AppUser, Long> {
	AppUser findByPassword(String password);
	AppUser findByUsername(String username);
	AppUser findByToken(String token);
	AppUser findById(long id);
}
