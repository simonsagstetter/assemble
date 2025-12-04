package com.assemble.backend.models.db;

import com.assemble.backend.models.core.BaseJPAEntitiy;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "greetings")
public class EntityGreeting extends BaseJPAEntitiy {

    @Schema(
            description = "Message",
            requiredMode = Schema.RequiredMode.REQUIRED,
            nullable = false,
            accessMode = Schema.AccessMode.READ_WRITE,
            example = "Hello World!"
    )
    @NotNull
    private String message;
}
