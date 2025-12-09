/*
 * assemble
 * BaseMongoEntity.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.entities.core;

import com.assemble.backend.models.entities.auth.UserAudit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Id
    @NonNull
    @NotBlank
    private String id;

    @NotNull
    @Version
    private Integer version;

    @NotNull
    @CreatedDate
    private Instant createdDate;

    @NotNull
    @LastModifiedDate
    private Instant lastModifiedDate;

    @NotNull
    @CreatedBy
    private UserAudit createdBy;

    @NotNull
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
