package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.controller.DuplicateException;
import ch.uzh.ifi.seal.soprafs19.controller.NonExistentUserException;
import ch.uzh.ifi.seal.soprafs19.entity.AppUser;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Iterable<AppUser> getUsers() {
        return this.userRepository.findAll();
    }


    //registration
    public AppUser createUser(AppUser newAppUser) {
        if(userRepository.findByUsername(newAppUser.getUsername())!=null) {
            throw new DuplicateException("Username already taken" + newAppUser.getUsername());
        }

        newAppUser.setToken(UUID.randomUUID().toString());
        newAppUser.setStatus(UserStatus.OFFLINE);
        newAppUser.setCreationDate((LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
        //newUser.setBirthday(newUser.getBirthday());
        userRepository.save(newAppUser);
        log.debug("Created Information for User: {}", newAppUser);
        return newAppUser;
    }

    //login
    public AppUser checkUser(AppUser newAppUser) {
        AppUser loginAppUser = userRepository.findByUsername(newAppUser.getUsername());
        if(loginAppUser != null && loginAppUser.getPassword().equals(newAppUser.getPassword())) {
            AppUser tempAppUser = userRepository.findByUsername(newAppUser.getUsername());
            tempAppUser.setStatus(UserStatus.ONLINE);
            tempAppUser.setToken(UUID.randomUUID().toString());
            userRepository.save(tempAppUser);
            return tempAppUser;
        }
        throw new NonExistentUserException("Name: "+ newAppUser.getPassword()+" Username: "+ newAppUser.getUsername());
    }

    //logout
    public AppUser logoutUser(AppUser newAppUser) {
        AppUser tempAppUser = userRepository.findByToken(newAppUser.getToken());
        tempAppUser.setStatus(UserStatus.OFFLINE);
        userRepository.save(tempAppUser);
        return tempAppUser;
    }

    //profile
    public AppUser getUser(long id) {
        AppUser tempAppUser = userRepository.findById(id);
        if(tempAppUser !=null) {
            return tempAppUser;
        }
        else {
            throw new NonExistentUserException("User couldn't be found");
        }
    }

    //update Username and/or Birthday
    public AppUser updateUser(AppUser newAppUser) {
        AppUser tempAppUser = userRepository.findByToken(newAppUser.getToken());
        if(userRepository.findByUsername(newAppUser.getUsername()) != null && userRepository.findByUsername(newAppUser.getUsername())!= tempAppUser) {
            throw new DuplicateException("User with that Username already exists, cannot change to it.");
        }
        else {
            tempAppUser.setBirthday(newAppUser.getBirthday());
            tempAppUser.setUsername(newAppUser.getUsername());
            userRepository.save(tempAppUser);
            return newAppUser;
        }
    }

}
