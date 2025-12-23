/*
 * assemble
 * ProjectRestController.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.controllers.rest.project;

import com.assemble.backend.models.dtos.global.ValidationErrorResponse;
import com.assemble.backend.models.dtos.project.ProjectCreateDTO;
import com.assemble.backend.models.dtos.project.ProjectDTO;
import com.assemble.backend.services.project.ProjectServiceImpl;
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
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Project endpoints")
@PreAuthorize("hasRole('MANAGER') || hasRole('ADMIN') || hasRole('SUPERUSER')")
@AllArgsConstructor
public class ProjectRestController {

    private ProjectServiceImpl projectService;

    @Operation(
            summary = "Get All Projects"
    )
    @ApiResponse(
            description = "OK",
            responseCode = "200",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(
                            schema = @Schema(
                                    implementation = ProjectDTO.class
                            )
                    )
            )
    )
    @GetMapping(
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        return ResponseEntity.ok( projectService.getAllProjects() );
    }

    @Operation(
            summary = "Get Project By Identifier"
    )
    @ApiResponse(
            description = "OK",
            responseCode = "200",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = ProjectDTO.class
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
    public ResponseEntity<ProjectDTO> getProjectById( @PathVariable String id ) {
        return ResponseEntity.ok( projectService.getProjectById( id ) );
    }

    @Operation(
            summary = "Create a project"
    )
    @ApiResponse(
            description = "Created",
            responseCode = "201",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = ProjectDTO.class
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
            responseCode = "409",
            description = "Conflict",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @PostMapping(
            path = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ProjectDTO> createProject(
            @Valid @RequestBody ProjectCreateDTO projectCreateDTO
    ) {
        return ResponseEntity.status( HttpStatus.CREATED )
                .body( projectService.createProject( projectCreateDTO ) );
    }

    @Operation(
            summary = "Delete Project By Identifier"
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
    public ResponseEntity<Void> deleteProjectById( @PathVariable String id ) {
        projectService.deleteProjectById( id );
        return ResponseEntity.noContent().build();
    }
}
