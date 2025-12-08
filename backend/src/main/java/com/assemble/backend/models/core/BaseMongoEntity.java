package com.assemble.backend.models.core;

import com.assemble.backend.models.auth.UserAudit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.Instant;

@SuperBuilder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseMongoEntity implements Persistable<String>, Serializable {

    @Schema(
            description = "unique identifier of the entity",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "uuid",
            accessMode = Schema.AccessMode.READ_ONLY,
            example = "ur123e4567e89b12d3a456426655440000"
    )
    @Id
    @NonNull
    private String id;


    @Schema(
            description = "creation timestamp of the entity",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "instant",
            accessMode = Schema.AccessMode.READ_ONLY,
            example = "1970-01-01T00:00:00.000Z"
    )
    @CreatedDate
    private Instant createdDate;


    @Schema(
            description = "modification timestamp of the entity",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "instant",
            accessMode = Schema.AccessMode.READ_ONLY,
            example = "1970-01-01T00:00:00.000Z"
    )
    @LastModifiedDate
    private Instant lastModifiedDate;


    @Schema(
            description = "user that created the entity",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @CreatedBy
    private UserAudit createdBy;


    @Schema(
            description = "user that last modified the entity",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @LastModifiedBy
    private UserAudit lastModifiedBy;

    @Override
    public boolean isNew() {
        return createdDate == null;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
