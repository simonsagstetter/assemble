package com.assemble.backend.models.db;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "greetings")
public class EntityGreeting {

    @Schema(
            description = "Unique identifier of this entity",
            requiredMode = Schema.RequiredMode.REQUIRED,
            nullable = false,
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Schema(
            description = "Message",
            requiredMode = Schema.RequiredMode.REQUIRED,
            nullable = false,
            accessMode = Schema.AccessMode.READ_WRITE
    )
    @NonNull
    private String message;
}
