/*
 * assemble
 * EmployeeRestController.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.controllers.rest.employee;

import com.assemble.backend.models.dtos.employee.*;
import com.assemble.backend.models.dtos.global.ErrorResponse;
import com.assemble.backend.models.dtos.global.ValidationErrorResponse;
import com.assemble.backend.services.employee.EmployeeService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employees", description = "Employee endpoints")
@PreAuthorize("hasRole('ADMIN') || hasRole('MANAGER') || hasRole('SUPERUSER')")
public class EmployeeRestController {

    private EmployeeService employeeService;

    @Operation(
            summary = "Get All Employees"
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = EmployeeDTO.class))
            )
    )
    @GetMapping(
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok( employeeService.getAllEmployees() );
    }

    @Operation(
            summary = "Get Employee By Id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = EmployeeDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<EmployeeDTO> getEmployee( @PathVariable String id ) {
        return ResponseEntity.ok( employeeService.getEmployeeById( id ) );
    }

    @Operation(
            summary = "Search unlinked employees by fullname"
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = EmployeeRefDTO.class))
            )
    )
    @GetMapping(
            path = "/search/{searchTerm}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<EmployeeRefDTO>> searchUnlinkedEmployees(
            @PathVariable String searchTerm
    ) {
        return ResponseEntity.ok( employeeService.searchUnlinkedEmployees( searchTerm ) );
    }

    @Operation(
            summary = "Create Employee"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = EmployeeDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(oneOf = { ErrorResponse.class, ValidationErrorResponse.class })
            )
    )
    @PostMapping(
            path = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<EmployeeDTO> createEmployee(
            @Valid @RequestBody EmployeeCreateDTO employeeCreateDTO
    ) {
        return ResponseEntity.status( HttpStatus.CREATED )
                .body( employeeService.createEmployee( employeeCreateDTO )
                );
    }

    @Operation(
            summary = "Update linked user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = EmployeeDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @PatchMapping(
            path = "/{id}/user",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<EmployeeDTO> updateEmployeeUser(
            @PathVariable String id,
            @Valid @RequestBody EmployeeUpdateUserDTO employeeUpdateUserDTO
    ) {
        return ResponseEntity.ok(
                employeeService.setEmployeeUser( id, employeeUpdateUserDTO )
        );
    }

    @Operation(
            summary = "Update employee"
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = EmployeeDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ValidationErrorResponse.class)
            )
    )
    @PatchMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable String id,
            @Valid @RequestBody EmployeeUpdateDTO employeeUpdateDTO
    ) {
        return ResponseEntity.ok(
                employeeService.updateEmployee( id, employeeUpdateDTO )
        );
    }

    @Operation(
            summary = "Delete employee"
    )
    @ApiResponse(
            responseCode = "204",
            description = "No Content"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee( @PathVariable String id ) {
        employeeService.deleteEmployee( id );
        return ResponseEntity.noContent().build();
    }
}
