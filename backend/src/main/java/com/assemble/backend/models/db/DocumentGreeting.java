package com.assemble.backend.models.db;

import com.mongodb.lang.NonNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document(value = "greetings")
public record DocumentGreeting(
        @Schema(
                description = "Unique identifier of this entity",
                requiredMode = Schema.RequiredMode.REQUIRED,
                nullable = false,
                accessMode = Schema.AccessMode.READ_ONLY
        )
        @Id
        String id,
        @Schema(
                description = "Message",
                requiredMode = Schema.RequiredMode.REQUIRED,
                nullable = false,
                accessMode = Schema.AccessMode.READ_WRITE
        )
        @NonNull
        String message
) {
}
