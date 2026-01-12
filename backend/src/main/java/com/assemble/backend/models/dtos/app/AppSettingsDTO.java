/*
 * assemble
 * AppSettingsDTO.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.dtos.app;

import com.assemble.backend.models.entities.employee.Address;
import com.assemble.backend.models.entities.holiday.SubdivisionCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@Schema
public class AppSettingsDTO {
    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NonNull
    @NotBlank
    private String companyName;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NonNull
    private Address companyAddress;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NonNull
    private SubdivisionCode holidaySubdivisionCode;
}
