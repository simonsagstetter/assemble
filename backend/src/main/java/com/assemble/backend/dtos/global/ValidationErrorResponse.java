package com.assemble.backend.dtos.global;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;


@SuperBuilder
@Getter
@Schema(name = "Validation Error Response", description = "Contains a list of field errors")
public class ValidationErrorResponse extends ErrorResponse {
    @Schema(
            description = "List of field errors",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private List<FieldValidationError> errors;
}
