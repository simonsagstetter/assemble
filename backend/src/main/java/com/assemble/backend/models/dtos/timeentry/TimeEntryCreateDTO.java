/*
 * assemble
 * TimeEntryCreateDTO.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.dtos.timeentry;

import com.assemble.backend.models.dtos.timeentry.validators.ValidTimeValues;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Duration;
import java.time.Instant;

@Data
@Builder
@Schema
@ValidTimeValues
public class TimeEntryCreateDTO {

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NonNull
    @NotBlank
    private String employeeId;

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NonNull
    @NotBlank
    private String projectId;

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NonNull
    @NotBlank
    @Size(min = 10, max = 1000)
    private String description;

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Instant startTime;

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Instant endTime;

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Duration pauseTime;

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Duration totalTime;
}
