/*
 * assemble
 * ProjectAssignmentDTO.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.dtos.project;

import com.assemble.backend.models.dtos.core.BaseEntityDTO;
import com.assemble.backend.models.dtos.employee.EmployeeRefDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema
public class ProjectAssignmentDTO extends BaseEntityDTO {

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NonNull
    private EmployeeRefDTO employee;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NonNull
    private ProjectRefDTO project;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private boolean active;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private BigDecimal hourlyRate;
}
