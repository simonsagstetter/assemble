/*
 * assemble
 * UserUpdatePasswordDTO.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.dtos.auth.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@Schema
public class UserUpdatePasswordDTO {

    @Schema(
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE
    )
    @NonNull
    @NotNull
    @Builder.Default
    boolean invalidateAllSessions = false;

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE,
            format = "password"
    )
    @NonNull
    @NotBlank
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!?.,;:-]).*$",
            message = "Password must contain at least one digit, lowercase, uppercase, and special character"
    )
    private String newPassword;
}
