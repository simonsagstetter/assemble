/*
 * assemble
 * ProjectDTO.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.dtos.project;

import com.assemble.backend.models.dtos.core.BaseEntityDTO;
import com.assemble.backend.models.entities.project.ProjectColor;
import com.assemble.backend.models.entities.project.ProjectStage;
import com.assemble.backend.models.entities.project.ProjectType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema
public class ProjectDTO extends BaseEntityDTO {

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

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private boolean active;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private ProjectType type;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private ProjectStage stage;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String category;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String description;

    @NonNull
    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private ProjectColor color;

}
