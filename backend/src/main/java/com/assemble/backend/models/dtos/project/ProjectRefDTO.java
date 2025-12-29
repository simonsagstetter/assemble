/*
 * assemble
 * ProjectRefDTO.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.dtos.project;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@Schema
public class ProjectRefDTO {

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
            example = "P00001"
    )
    @NonNull
    private String no;

    @NonNull
    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;
}
