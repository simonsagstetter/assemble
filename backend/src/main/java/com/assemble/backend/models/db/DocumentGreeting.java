package com.assemble.backend.models.db;

import com.assemble.backend.models.core.BaseMongoEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "greetings")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DocumentGreeting extends BaseMongoEntity {

    @Schema(
            description = "Message",
            requiredMode = Schema.RequiredMode.REQUIRED,
            nullable = false,
            accessMode = Schema.AccessMode.READ_WRITE
    )
    @NotNull
    private String message;
}
