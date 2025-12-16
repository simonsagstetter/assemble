/*
 * assemble
 * UserAdminRestController.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.controllers.rest.auth;

import com.assemble.backend.models.dtos.auth.admin.*;
import com.assemble.backend.models.dtos.global.ValidationErrorResponse;
import com.assemble.backend.services.auth.UserAdminService;
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
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN') || hasRole('SUPERUSER')")
@AllArgsConstructor
@Tag(name = "UserManagement", description = "User management for admins")
public class UserAdminRestController {

    private final UserAdminService userAdminService;

    @Operation(
            summary = "Get All Users"
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = UserAdminDTO.class))
            )
    )
    @GetMapping(
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<UserAdminDTO>> getAllUsers() {
        return ResponseEntity.ok( userAdminService.getAllUsers() );
    }

    @Operation(
            summary = "Get User by Id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserAdminDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Not Found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserAdminDTO> getUserById( @PathVariable String id ) {
        return ResponseEntity.ok( userAdminService.getUserById( id ) );
    }

    @Operation(
            summary = "Get All Unlinked Users"
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = UserAdminDTO.class))
            )
    )
    @GetMapping(
            path = "/unlinked",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<UserAdminDTO>> getAllUnlinkedUsers() {
        return ResponseEntity.ok( userAdminService.getUnlinkedUsers() );
    }

    @Operation(
            summary = "Create User"
    )
    @ApiResponse(
            responseCode = "201",
            description = "CREATED",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserAdminDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Not Found",
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
    public ResponseEntity<UserAdminDTO> createUser( @Valid @RequestBody UserCreateDTO userCreateDTO ) {
        return ResponseEntity.status( HttpStatus.CREATED )
                .body( userAdminService.createUser( userCreateDTO ) );
    }

    @Operation(
            summary = "Update User"
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserAdminDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Not Found",
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
    @PatchMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserAdminDTO> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UserUpdateDTO userUpdateDTO
    ) {
        return ResponseEntity.ok( userAdminService.updateUser( id, userUpdateDTO ) );
    }

    @Operation(
            summary = "Update User Status"
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserAdminDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Not Found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @PatchMapping(
            path = "/{id}/status",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserAdminDTO> updateUserStatus(
            @PathVariable String id,
            @Valid @RequestBody UserUpdateStatusDTO userUpdateStatusDTO
    ) {
        return ResponseEntity.ok( userAdminService
                .setUserStatus(
                        id,
                        userUpdateStatusDTO
                )
        );
    }

    @Operation(
            summary = "Update User Password"
    )
    @ApiResponse(
            responseCode = "204",
            description = "No Content"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Not Found",
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
            path = "/{id}/password",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> updateUserPassword(
            @PathVariable String id,
            @Valid @RequestBody UserUpdatePasswordDTO userUpdatePasswordDTO
    ) {
        userAdminService.setUserPassword(
                id,
                userUpdatePasswordDTO.getNewPassword(),
                userUpdatePasswordDTO.getInvalidateAllSessions()
        );

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update User Roles"
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    schema = @Schema(implementation = UserAdminDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Not Found",
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
            path = "/{id}/roles",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserAdminDTO> updateUserRoles(
            @PathVariable String id,
            @Valid @RequestBody UserUpdateRolesDTO userUpdateRolesDTO
    ) {
        return ResponseEntity.ok(
                userAdminService
                        .setUserRoles(
                                id,
                                userUpdateRolesDTO.getRoles()
                        )
        );
    }

    @Operation(
            summary = "Update User Employee"
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    schema = @Schema(implementation = UserAdminDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Not Found",
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
    @PatchMapping(
            path = "/{id}/employee",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserAdminDTO> updateUserEmployee(
            @PathVariable String id,
            @Valid @RequestBody UserUpdateEmployeeDTO userUpdateEmployeeDTO
    ) {
        return ResponseEntity.ok(
                userAdminService
                        .setUserEmployee(
                                id,
                                userUpdateEmployeeDTO
                        )
        );
    }

    @Operation(
            summary = "Delete User"
    )
    @ApiResponse(
            responseCode = "204",
            description = "No Content"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Not Found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById( @PathVariable String id ) {
        userAdminService.deleteUser( id );
        return ResponseEntity.noContent().build();
    }
}
