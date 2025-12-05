package com.assemble.backend.models.db;

import com.assemble.backend.models.core.BaseJPAEntity;
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
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EntityGreeting extends BaseJPAEntity {

    @Schema(
            description = "Message",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE,
            example = "Hello World!"
    )
    @NotNull
    private String message;
}
