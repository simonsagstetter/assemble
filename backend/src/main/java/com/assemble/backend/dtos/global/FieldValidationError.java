package com.assemble.backend.dtos.global;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
@Schema(name = "Field Validation Error", description = "Contains information about a field validation error")
public class FieldValidationError {

    @Schema(
            description = "field where the error occurred",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @NonNull
    private String fieldName;

    @Schema(
            description = "detailed information about the error",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @NonNull
    private String errorMessage;
}
