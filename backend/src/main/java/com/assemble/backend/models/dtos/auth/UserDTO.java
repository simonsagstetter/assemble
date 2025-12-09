package com.assemble.backend.models.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@Schema(name = "User", description = "Contains information about a user")
public class UserDTO {

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            format = "uuid",
            example = "6c0a7fd9872e47b29ec68fb10ea251de"
    )
    @NonNull
    private String id;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            example = "maxmustermann"
    )
    @NonNull
    private String username;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            example = "max.mustermann@example.com",
            format = "email"
    )
    @NonNull
    private String email;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            example = "Max"
    )
    @NonNull
    private String firstname;

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            example = "Mustermann"
    )
    @NonNull
    private String lastname;
}
