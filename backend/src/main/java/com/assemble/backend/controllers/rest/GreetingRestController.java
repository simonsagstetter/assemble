package com.assemble.backend.controllers.rest;

import com.assemble.backend.models.db.EntityGreeting;
import com.assemble.backend.repositories.EntityRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/greetings")
@Tag(name = "Greetings", description = "CRUD endpoint for the greeting entity")
public class GreetingRestController {

    private final EntityRepository entityRepository;

    public GreetingRestController( EntityRepository entityRepository ) {
        this.entityRepository = entityRepository;
    }

    @Operation(
            summary = "Get all greetings",
            description = "Returns a list of all stored greetings"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of Greeting",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = EntityGreeting.class))
            )
    )
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EntityGreeting>> getAllGreetings() {
        return ResponseEntity.ok( this.entityRepository.findAll() );
    }
}
