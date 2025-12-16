/*
 * assemble
 * UserUpdateRolesDTO.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.dtos.auth.admin;

import com.assemble.backend.models.entities.auth.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Builder
@Schema
public class UserUpdateRolesDTO {

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "UserRole",
            example = "[ADMIN,USER]"
    )
    @NonNull
    @NotEmpty(message = "User roles are required")
    private List<UserRole> roles;
}
