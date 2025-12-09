package com.assemble.backend.controllers.rest;

import com.assemble.backend.models.entities.db.EntityGreeting;
import com.assemble.backend.repositories.db.EntityRepository;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/greetings")
@Hidden
public class GreetingRestController {

    private final EntityRepository entityRepository;

    public GreetingRestController( EntityRepository entityRepository ) {
        this.entityRepository = entityRepository;
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EntityGreeting>> getAllGreetings() {
        return ResponseEntity.ok( this.entityRepository.findAll() );
    }
}
