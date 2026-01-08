/*
 * assemble
 * TimeEntryUpdateDTO.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.dtos.timeentry;

import com.assemble.backend.models.dtos.timeentry.validators.TimeValidatable;
import com.assemble.backend.models.dtos.timeentry.validators.ValidTimeValues;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@Schema
@ValidTimeValues
public class TimeEntryUpdateDTO implements TimeValidatable {
    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Nullable
    @Size(min = 1)
    private String employeeId;

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Nullable
    @Size(min = 1)
    private String projectId;

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Nullable
    @Size(min = 10, max = 1000)
    private String description;

    @Nullable
    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private LocalDate date;

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Nullable
    private Instant startTime;

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Nullable
    private Instant endTime;

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Nullable
    private Duration pauseTime;

    @Schema(
            accessMode = Schema.AccessMode.READ_WRITE,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Nullable
    private Duration totalTime;
}
