/*
 * assemble
 * BaseJPAEntity.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.entities.core;

import com.assemble.backend.models.entities.auth.UserAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseJPAEntity
        implements Persistable<String>, Serializable {

    @Id
    @NonNull
    @NotBlank
    @Column(nullable = false, unique = true, name = "ID", updatable = false)
    private String id;

    @NotNull
    @Version
    @Column(name = "VERSION", nullable = false)
    private Integer version;

    @NotNull
    @CreatedDate
    @Column(nullable = false, updatable = false, name = "CREATED_DATE")
    private Instant createdDate;

    @NotNull
    @LastModifiedDate
    @Column(nullable = false, name = "LAST_MODIFIED_DATE")
    private Instant lastModifiedDate;

    @NotNull
    @CreatedBy
    @AttributeOverride(name = "id", column = @Column(name = "CREATED_BY_ID", updatable = false))
    @AttributeOverride(name = "username", column = @Column(name = "CREATED_BY_USERNAME", nullable = false, updatable = false))
    private UserAudit createdBy;

    @NotNull
    @LastModifiedBy
    @AttributeOverride(name = "id", column = @Column(name = "LAST_MODIFIED_BY_ID"))
    @AttributeOverride(name = "username", column = @Column(name = "LAST_MODIFIED_BY_USERNAME", nullable = false))
    private UserAudit lastModifiedBy;

    @JsonIgnore
    @Transient
    @Builder.Default
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
}
