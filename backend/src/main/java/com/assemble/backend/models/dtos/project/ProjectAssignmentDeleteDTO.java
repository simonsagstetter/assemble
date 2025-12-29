/*
 * assemble
 * ProjectAssignmentDeleteDTO.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.dtos.project;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Builder
@Schema
public class ProjectAssignmentDeleteDTO {

    @NonNull
    @Size(min = 1)
    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE,
            minimum = "1"
    )
    private List<String> ids;
}
