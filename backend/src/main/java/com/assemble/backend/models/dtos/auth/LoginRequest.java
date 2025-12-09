/*
 * assemble
 * LoginRequest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Schema(name = "LoginRequest", description = "credentials of a user")
public class LoginRequest {
    @Schema(
            description = "username",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE,
            example = "mustermannmax"
    )
    @NonNull
    @NotBlank
    private String username;
    @Schema(
            description = "password",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE,
            example = "**********"
    )
    @NonNull
    @NotBlank
    private String password;
}
