/*
 * assemble
 * HolidayDTO.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.dtos.holiday;

import com.assemble.backend.models.entities.holiday.Subdivision;
import com.assemble.backend.models.entities.holiday.api.TemporalScope;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@Schema
public class HolidayDTO {

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "uuid"
    )
    @NonNull
    private String id;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NonNull
    private String name;

    private LocalDate startDate;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NonNull
    private LocalDate endDate;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NonNull
    private TemporalScope temporalScope;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NonNull
    private Boolean nationWide;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NonNull
    private List<Subdivision> subdivisions;

}
