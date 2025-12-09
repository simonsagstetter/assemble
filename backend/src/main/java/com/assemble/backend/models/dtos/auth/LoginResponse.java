package com.assemble.backend.models.dtos.auth;

import com.assemble.backend.models.dtos.global.BaseResponse;
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
@Schema(name = "LoginResponse", description = "Login response entity")
public class LoginResponse extends BaseResponse {

    @Schema(
            description = "Human readable message",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            example = "Login Successful"
    )
    @NonNull
    @NotBlank
    @Builder.Default
    private String message = "Login Successful";

    @Schema(
            description = "Session Identifier",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            example = "1234567890"
    )
    @NonNull
    @NotBlank
    private String sessionId;
}
