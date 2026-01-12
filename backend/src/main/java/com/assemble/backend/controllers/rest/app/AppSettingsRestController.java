/*
 * assemble
 * AppSettingsRestController.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.controllers.rest.app;

import com.assemble.backend.models.dtos.app.AppSettingsDTO;
import com.assemble.backend.models.dtos.global.ValidationErrorResponse;
import com.assemble.backend.services.app.AppSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/settings")
@PreAuthorize("hasRole('ADMIN')||hasRole('SUPERUSER')")
@Tag(name = "AppSettings", description = "Application settings endpoints")
@AllArgsConstructor
public class AppSettingsRestController {

    private AppSettingsService service;

    @Operation(
            summary = "Get App Settings"
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = AppSettingsDTO.class
                    )
            )
    )
    @GetMapping(
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AppSettingsDTO> getAppSettings() {
        return ResponseEntity.ok( service.getSettings() );
    }

    @Operation(
            summary = "Update App Settings"
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = AppSettingsDTO.class
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = ValidationErrorResponse.class
                    )
            )
    )
    @PutMapping(
            path = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AppSettingsDTO> updateAppSettings( @RequestBody @Valid AppSettingsDTO settingsDTO ) {
        return ResponseEntity.ok( service.updateSettings( settingsDTO ) );
    }
}
