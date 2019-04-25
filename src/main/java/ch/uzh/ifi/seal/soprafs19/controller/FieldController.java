package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.Field;
import ch.uzh.ifi.seal.soprafs19.service.FieldService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//@RestController
public class FieldController {

    private final FieldService fieldService;

    private FieldController(FieldService fieldService) {
        this.fieldService = fieldService;
    }
}