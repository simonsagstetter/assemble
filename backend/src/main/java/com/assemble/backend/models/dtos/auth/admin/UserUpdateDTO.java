/*
 * assemble
 * UserUpdateDTO.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.dtos.auth.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@Builder
@Schema
public class UserUpdateDTO {

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.REQUIRED,
            minimum = "4",
            maximum = "20",
            example = "mustermannmax"
    )
    @Nullable
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters long")
    private String username;

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Max"
    )
    @Nullable
    @Size(min = 1)
    private String firstname;

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Musterperson"
    )
    @Nullable
    @Size(min = 1)
    private String lastname;

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "email",
            example = "max.musterperson@example.com"
    )
    @Nullable
    @Email(message = "Please provide a valid email address")
    private String email;

}
