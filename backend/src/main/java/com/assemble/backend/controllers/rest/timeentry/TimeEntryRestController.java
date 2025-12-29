/*
 * assemble
 * TimeEntryRestController.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.controllers.rest.timeentry;

import com.assemble.backend.models.dtos.global.ErrorResponse;
import com.assemble.backend.models.dtos.global.ValidationErrorResponse;
import com.assemble.backend.models.dtos.timeentry.TimeEntryCreateDTO;
import com.assemble.backend.models.dtos.timeentry.TimeEntryDTO;
import com.assemble.backend.services.timeentry.TimeEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timeentries")
@Tag(name = "Timeentries", description = "Timeentry endpoints")
@AllArgsConstructor
public class TimeEntryRestController {

    private TimeEntryService timeEntryService;

    @Operation(
            summary = "Get All Timentries"
    )
    @ApiResponse(
            description = "OK",
            responseCode = "200",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(
                            schema = @Schema(
                                    implementation = TimeEntryDTO.class
                            )
                    )
            )
    )
    @GetMapping(
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<TimeEntryDTO>> getAllTimeEntries() {
        return ResponseEntity.ok( timeEntryService.getAllTimeEntries() );
    }

    @Operation(
            summary = "Get All Timeentries by Project ID"
    )
    @ApiResponse(
            description = "OK",
            responseCode = "200",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(
                            schema = @Schema(
                                    implementation = TimeEntryDTO.class
                            )
                    )
            )
    )
    @GetMapping(
            path = "/project/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<TimeEntryDTO>> getAllTimeEntriesByProjectId(
            @PathVariable String id
    ) {
        return ResponseEntity.ok( timeEntryService.getTimeEntriesByProjectId( id ) );
    }

    @Operation(
            summary = "Get All Timeentries by Employee ID"
    )
    @ApiResponse(
            description = "OK",
            responseCode = "200",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(
                            schema = @Schema(
                                    implementation = TimeEntryDTO.class
                            )
                    )
            )
    )
    @GetMapping(
            path = "/employee/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<TimeEntryDTO>> getAllTimeEntriesByEmployeeId(
            @PathVariable String id
    ) {
        return ResponseEntity.ok( timeEntryService.getTimeEntriesByEmployeeId( id ) );
    }

    @Operation(
            summary = "Get Timeentry by ID"
    )
    @ApiResponse(
            description = "OK",
            responseCode = "200",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = TimeEntryDTO.class
                    )
            )
    )
    @ApiResponse(
            description = "Not Found",
            responseCode = "404",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = ErrorResponse.class
                    )
            )
    )
    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TimeEntryDTO> getTimeEntryById( @PathVariable String id ) {
        return ResponseEntity.ok( timeEntryService.getTimeEntryById( id ) );
    }

    @Operation(
            summary = "Create a timeentry"
    )
    @ApiResponse(
            description = "Created",
            responseCode = "201",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = TimeEntryDTO.class
                    )
            )
    )
    @ApiResponse(
            description = "Not Found",
            responseCode = "404",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = ErrorResponse.class
                    )
            )
    )
    @ApiResponse(
            description = "Bad Request",
            responseCode = "400",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = ValidationErrorResponse.class
                    )
            )
    )
    @PostMapping(
            path = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TimeEntryDTO> createTimeEntry(
            @Valid @RequestBody TimeEntryCreateDTO timeEntryCreateDTO
    ) {
        return ResponseEntity.status( HttpStatus.CREATED )
                .body( timeEntryService.createTimeEntry( timeEntryCreateDTO ) );
    }

    @Operation(
            summary = "Delete timeentry"
    )
    @ApiResponse(
            description = "No Content",
            responseCode = "204"
    )
    @ApiResponse(
            description = "Not Found",
            responseCode = "404"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeEntryById( @PathVariable String id ) {
        timeEntryService.deleteTimeEntryById( id );
        return ResponseEntity.noContent().build();
    }
}
