package com.assemble.backend.models.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Auditable;

import java.time.Instant;
import java.util.Optional;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseMongoEntity implements Auditable<String, String, Instant> {

    @Schema(
            description = "unique identifier of the entity",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "uuid",
            accessMode = Schema.AccessMode.READ_ONLY,
            example = "ur123e4567e89b12d3a456426655440000"
    )
    @Id
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
    private String createdBy;


    @Schema(
            description = "user that last modified the entity",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @LastModifiedBy
    private String lastModifiedBy;

    @Override
    public boolean isNew() {
        return createdDate == null;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Optional<String> getCreatedBy() {
        return Optional.ofNullable( createdBy );
    }

    @Override
    public void setCreatedBy( String createdBy ) {
        this.createdBy = createdBy;
    }

    @Override
    public Optional<Instant> getCreatedDate() {
        return Optional.ofNullable( createdDate );
    }

    @Override
    public void setCreatedDate( Instant creationDate ) {
        this.createdDate = creationDate;
    }

    @Override
    public Optional<String> getLastModifiedBy() {
        return Optional.ofNullable( lastModifiedBy );
    }

    @Override
    public void setLastModifiedBy( String lastModifiedBy ) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public Optional<Instant> getLastModifiedDate() {
        return Optional.ofNullable( lastModifiedDate );
    }

    @Override
    public void setLastModifiedDate( Instant lastModifiedDate ) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
