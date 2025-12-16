/*
 * assemble
 * SessionRestController.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.controllers.rest.auth;

import com.assemble.backend.models.dtos.auth.admin.SessionCountDTO;
import com.assemble.backend.models.dtos.auth.admin.SessionDTO;
import com.assemble.backend.services.auth.SessionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/sessions")
@Tag(name = "SessionManagement", description = "Session endpoints")
@PreAuthorize("hasRole('ADMIN') || hasRole('SUPERUSER')")
@Validated
public class SessionRestController {

    private final SessionService sessionService;

    @GetMapping("/{principalName}/count")
    public ResponseEntity<SessionCountDTO> getActiveUserSessionCount(
            @PathVariable @NotBlank String principalName
    ) {
        return ResponseEntity.ok( sessionService.getActiveUserSessionsCount( principalName ) );
    }


    @GetMapping("/{principalName}")
    public ResponseEntity<List<SessionDTO>> getUserSessionDetails(
            @PathVariable @NotBlank String principalName
    ) {
        return ResponseEntity.ok( sessionService.getUserSessionDetails( principalName ) );
    }

    @DeleteMapping("/{principalName}")
    public ResponseEntity<Void> invalidateUserSessions(
            @PathVariable @NotBlank String principalName
    ) {
        sessionService.invalidateUserSessions( principalName );
        return ResponseEntity.noContent().build();
    }
}
