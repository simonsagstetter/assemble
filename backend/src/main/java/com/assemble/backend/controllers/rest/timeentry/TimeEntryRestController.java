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
import com.assemble.backend.models.dtos.timeentry.TimeEntryUpdateDTO;
import com.assemble.backend.models.entities.auth.SecurityUser;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            summary = "Get Own Timeentries"
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
            path = "/me",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<TimeEntryDTO>> getOwnTimeEntries( @AuthenticationPrincipal SecurityUser user ) {
        return ResponseEntity.ok( timeEntryService.getOwnTimeEntries( user ) );
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
            summary = "Get Own Timeentry by ID"
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
    @ApiResponse(
            description = "Forbidden",
            responseCode = "403",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = ErrorResponse.class
                    )
            )
    )
    @GetMapping(
            path = "/me/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TimeEntryDTO> getOwnTimeEntryById(
            @AuthenticationPrincipal SecurityUser user,
            @PathVariable String id
    ) {
        return ResponseEntity.ok( timeEntryService.getOwnTimeEntryById( id, user ) );
    }

    @Operation(
            summary = "Create own timeentry"
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
            description = "Forbidden",
            responseCode = "403",
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
            path = "/me",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TimeEntryDTO> createOwnTimeEntry(
            @AuthenticationPrincipal SecurityUser user,
            @Valid @RequestBody TimeEntryCreateDTO timeEntryCreateDTO
    ) {
        return ResponseEntity.status( HttpStatus.CREATED )
                .body( timeEntryService.createOwnTimeEntry( timeEntryCreateDTO, user ) );
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
    @PreAuthorize("hasRole('ROLE_ADMIN')||hasRole('ROLE_MANAGER')||hasRole('ROLE_SUPERUSER')")
    public ResponseEntity<TimeEntryDTO> createTimeEntry(
            @Valid @RequestBody TimeEntryCreateDTO timeEntryCreateDTO
    ) {
        return ResponseEntity.status( HttpStatus.CREATED )
                .body( timeEntryService.createTimeEntry( timeEntryCreateDTO ) );
    }

    @Operation(
            summary = "Update own timeentry"
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
    @ApiResponse(
            description = "Forbidden",
            responseCode = "403",
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
    @PatchMapping(
            path = "/me/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TimeEntryDTO> updateOwnTimeEntry(
            @AuthenticationPrincipal SecurityUser user,
            @PathVariable String id,
            @Valid @RequestBody TimeEntryUpdateDTO timeEntryUpdateDTO
    ) {
        return ResponseEntity.ok( timeEntryService.updateOwnTimeEntry( id, timeEntryUpdateDTO, user ) );
    }

    @Operation(
            summary = "Update a timeentry"
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
    @PatchMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')||hasRole('ROLE_MANAGER')||hasRole('ROLE_SUPERUSER')")
    public ResponseEntity<TimeEntryDTO> updateTimeEntry(
            @PathVariable String id,
            @Valid @RequestBody TimeEntryUpdateDTO timeEntryUpdateDTO
    ) {
        return ResponseEntity.ok( timeEntryService.updateTimeEntry( id, timeEntryUpdateDTO ) );
    }

    @Operation(
            summary = "Delete own timeentry"
    )
    @ApiResponse(
            description = "No Content",
            responseCode = "204"
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
            description = "Forbidden",
            responseCode = "403",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = ErrorResponse.class
                    )
            )
    )
    @DeleteMapping("/me/{id}")
    public ResponseEntity<Void> deleteOwnTimeEntryById(
            @AuthenticationPrincipal SecurityUser user,
            @PathVariable String id
    ) {
        timeEntryService.deleteOwnTimeEntryById( id, user );
        return ResponseEntity.noContent().build();
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
            responseCode = "404",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = ErrorResponse.class
                    )
            )
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')||hasRole('ROLE_MANAGER')||hasRole('ROLE_SUPERUSER')")
    public ResponseEntity<Void> deleteTimeEntryById( @PathVariable String id ) {
        timeEntryService.deleteTimeEntryById( id );
        return ResponseEntity.noContent().build();
    }
}
