package com.assemble.backend.models.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

@Data
@Schema(name = "Change Password", description = "Contains information about a change password request")
public class ChangePasswordDTO {

    @Schema(
            description = "The current password of the user",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE,
            example = "**********",
            format = "password"
    )
    @NonNull
    @NotBlank
    private String oldPassword;

    @Schema(
            description = "The new password for the user",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE,
            example = "**********",
            format = "password"
    )
    @NonNull
    @NotBlank
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern.List({
            @Pattern(regexp = ".*[0-9].*", message = "Password must contain at least one digit"),
            @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter"),
            @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter"),
            @Pattern(regexp = ".*[@#$%^&+=!?.,;:-].*", message = "Password must contain at least one special character")
    })
    private String newPassword;
}
