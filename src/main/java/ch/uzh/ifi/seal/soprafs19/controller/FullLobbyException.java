package ch.uzh.ifi.seal.soprafs19.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) //error 409
public class FullLobbyException extends RuntimeException{
    public FullLobbyException(String exception) {
        super(exception);
    }
}