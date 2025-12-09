package com.assemble.backend.models.dtos.global;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(name = "ErrorResponse", description = "Error response entity")
public class ErrorResponse extends BaseResponse {
    @Schema(
            description = "Human readable error message",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @NonNull
    @NotBlank
    private String message;
}
