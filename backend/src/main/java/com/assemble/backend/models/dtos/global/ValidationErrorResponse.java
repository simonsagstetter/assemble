/*
 * assemble
 * ValidationErrorResponse.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.dtos.global;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@Schema(name = "ValidationErrorResponse", description = "Contains a list of field errors")
public class ValidationErrorResponse extends ErrorResponse {
    @Schema(
            description = "List of field errors",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private List<FieldValidationError> errors;
}
