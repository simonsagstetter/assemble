/*
 * assemble
 * ProjectAssignmentRestController.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.controllers.rest.project;

import com.assemble.backend.models.dtos.global.ErrorResponse;
import com.assemble.backend.models.dtos.global.ValidationErrorResponse;
import com.assemble.backend.models.dtos.project.ProjectAssignmentCreateDTO;
import com.assemble.backend.models.dtos.project.ProjectAssignmentDTO;
import com.assemble.backend.models.entities.auth.SecurityUser;
import com.assemble.backend.services.project.ProjectAssignmentService;
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
@RequestMapping("/api/projectassignments")
@AllArgsConstructor
@Tag(name = "ProjectAssignments", description = "ProjectAssignment endpoints")
@PreAuthorize("hasRole('MANAGER') || hasRole('ADMIN') || hasRole('SUPERUSER')")
public class ProjectAssignmentRestController {

    private ProjectAssignmentService service;

    @Operation(
            summary = "Get Project Assignment By ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = ProjectAssignmentDTO.class
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
    public ResponseEntity<ProjectAssignmentDTO> getProjectAssignmentById(
            @PathVariable String id
    ) {
        return ResponseEntity.ok( service.getProjectAssignmentById( id ) );
    }

    @Operation(
            summary = "Get All Assignments By Project ID"
    )
    @ApiResponse(
            description = "OK",
            responseCode = "200",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(
                            schema = @Schema(
                                    implementation = ProjectAssignmentDTO.class
                            )
                    )
            )
    )
    @GetMapping(
            path = "/project/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<ProjectAssignmentDTO>> getAllProjectAssignmentsByProjectId(
            @PathVariable String id
    ) {
        return ResponseEntity.ok( service.getProjectAssignmentsByProjectId( id ) );
    }

    @Operation(
            summary = "Get All Assignments By Employee ID"
    )
    @ApiResponse(
            description = "OK",
            responseCode = "200",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(
                            schema = @Schema(
                                    implementation = ProjectAssignmentDTO.class
                            )
                    )
            )
    )
    @GetMapping(
            path = "/employee/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<ProjectAssignmentDTO>> getAllProjectAssignmentsByEmployeeId(
            @PathVariable String id
    ) {
        return ResponseEntity.ok( service.getProjectAssignmentsByEmployeeId( id ) );
    }

    @Operation(
            summary = "Get Own Assignments"
    )
    @ApiResponse(
            description = "OK",
            responseCode = "200",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(
                            schema = @Schema(
                                    implementation = ProjectAssignmentDTO.class
                            )
                    )
            )
    )
    @GetMapping(
            path = "/me",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('USER') || hasRole('MANAGER') || hasRole('ADMIN') || hasRole('SUPERUSER')")
    public ResponseEntity<List<ProjectAssignmentDTO>> getOwnProjectAssignments( @AuthenticationPrincipal SecurityUser user ) {
        return ResponseEntity.ok( service.getOwnProjectAssignments( user ) );
    }

    @Operation(
            summary = "Create ProjectAssignment"
    )
    @ApiResponse(
            description = "Created",
            responseCode = "201",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = ProjectAssignmentDTO.class
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
            description = "Conflict",
            responseCode = "409",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = ErrorResponse.class
                    )
            )
    )
    @PostMapping(
            path = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ProjectAssignmentDTO> createProjectAssignment(
            @RequestBody @Valid ProjectAssignmentCreateDTO projectAssignmentCreateDTO
    ) {
        return ResponseEntity.status( HttpStatus.CREATED )
                .body( service.createProjectAssignment( projectAssignmentCreateDTO ) );
    }

    @Operation(
            summary = "Delete ProjectAssignments"
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
    public ResponseEntity<Void> deleteProjectAssignmentById(
            @PathVariable String id
    ) {
        service.deleteProjectAssignmentById( id );
        return ResponseEntity.noContent().build();
    }
}
