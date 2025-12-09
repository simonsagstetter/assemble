/*
 * assemble
 * DocumentGreeting.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.entities.db;

import com.assemble.backend.models.entities.core.BaseMongoEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document("greetings")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DocumentGreeting extends BaseMongoEntity {

    @NonNull
    @NotBlank
    private String message;
}
