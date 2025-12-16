/*
 * assemble
 * UserUpdateEmployeeDTO.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.dtos.auth.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema
public class UserUpdateEmployeeDTO {
    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            format = "uuid"
    )
    @Nullable
    private String employeeId;
}
