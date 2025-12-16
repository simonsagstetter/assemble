/*
 * assemble
 * UserCreateDTO.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.dtos.auth.admin;

import com.assemble.backend.models.entities.auth.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Builder
@Schema(name = "UserCreateDTO", description = "DTO for creating user entities")
public class UserCreateDTO {

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "4",
            maximum = "20",
            example = "mustermannmax"
    )
    @NonNull
    @NotBlank
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters long")
    private String username;

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            format = "password",
            minimum = "8",
            maximum = "20",
            example = "SuperS3cret!Pa$$worD"
    )
    @Nullable
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one digit")
    @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
    @Pattern(regexp = ".*[@#$%^&+=!?.,;:-].*", message = "Password must contain at least one special character")
    private String password;

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Max"
    )
    @NonNull
    @NotBlank
    private String firstname;

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Musterperson"
    )
    @NonNull
    @NotBlank
    private String lastname;

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "email",
            example = "max.musterperson@example.com"
    )
    @NonNull
    @Email(message = "Please provide a valid email address")
    private String email;

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "UserRole",
            example = "[ADMIN,USER]"
    )
    @NonNull
    @NotEmpty(message = "User roles are required")
    private List<UserRole> roles;

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Nullable
    @Builder.Default
    private boolean enabled = true;

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Nullable
    private String employeeId;
}
