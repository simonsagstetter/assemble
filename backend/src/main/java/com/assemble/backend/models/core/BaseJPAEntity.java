package com.assemble.backend.models.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Optional;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseJPAEntity
        implements Auditable<String, String, Instant> {

    @Schema(
            description = "unique identifier of the entity",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "uuid",
            accessMode = Schema.AccessMode.READ_ONLY,
            example = "ur123e4567e89b12d3a456426655440000"
    )
    @Id
    @Column(nullable = false, unique = true, name = "ID")
    private String id;


    @Schema(
            description = "creation timestamp of the entity",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "instant",
            accessMode = Schema.AccessMode.READ_ONLY,
            example = "1970-01-01T00:00:00.000Z"
    )
    @CreatedDate
    @Column(nullable = false, updatable = false, name = "CREATED_DATE")
    private Instant createdDate;


    @Schema(
            description = "modification timestamp of the entity",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "instant",
            accessMode = Schema.AccessMode.READ_ONLY,
            example = "1970-01-01T00:00:00.000Z"
    )
    @LastModifiedDate
    @Column(nullable = false, name = "LAST_MODIFIED_DATE")
    private Instant lastModifiedDate;


    @Schema(
            description = "user that created the entity",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @CreatedBy
    @Column(name = "CREATED_BY", nullable = false, updatable = false)
    private String createdBy;


    @Schema(
            description = "user that last modified the entity",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY
    )
    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY", nullable = false)
    private String lastModifiedBy;

    @JsonIgnore
    @Schema(hidden = true)
    @Transient
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostPersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public void setId( String id ) {
        this.id = id;
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
