/*
 * assemble
 * UserAdminDTO.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.dtos.auth.admin;

import com.assemble.backend.models.dtos.employee.EmployeeRefDTO;
import com.assemble.backend.models.entities.auth.UserAudit;
import com.assemble.backend.models.entities.auth.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@Schema(name = "UserAdmin", description = "Contains all information about a user including administrative fields")
public class UserAdminDTO {
    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "uuid",
            example = "6c0a7fd9872e47b29ec68fb10ea251de"
    )
    @NonNull
    private String id;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "maxmustermann"
    )
    @NonNull
    private String username;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "max.mustermann@example.com",
            format = "email"
    )
    @NonNull
    private String email;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Max"
    )
    @NonNull
    private String firstname;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Mustermann"
    )
    @NonNull
    private String lastname;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Max Mustermann"
    )
    @NonNull
    private String fullname;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "[USER,ADMIN]"
    )
    @NonNull
    private List<UserRole> roles;

    @NonNull
    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Boolean enabled;

    @NonNull
    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Boolean locked;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private EmployeeRefDTO employee;

    @NonNull
    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Instant createdDate;

    @NonNull
    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UserAudit createdBy;

    @NonNull
    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Instant lastModifiedDate;

    @NonNull
    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UserAudit lastModifiedBy;
}
