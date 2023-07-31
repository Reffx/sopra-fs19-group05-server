package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.AppUser;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    Iterable<AppUser> all() {
        return service.getUsers();
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    AppUser createUser(@RequestBody AppUser newAppUser) {
        return this.service.createUser(newAppUser);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    AppUser checkUser(@RequestBody AppUser newAppUser) {
        return this.service.checkUser(newAppUser);
    }

    @GetMapping("/users/{id}")
    AppUser getUser(@PathVariable String id) {
        return this.service.getUser(Long.parseLong(id));
    }

    @CrossOrigin
    @PutMapping("/users")
    AppUser logoutUser(@RequestBody AppUser newAppUser) {
        return this.service.logoutUser(newAppUser);
    }

    @CrossOrigin
    @PutMapping("/users/{id}")
    AppUser updateUser(@RequestBody AppUser newAppUser) {
        return this.service.updateUser(newAppUser);
    }

}
