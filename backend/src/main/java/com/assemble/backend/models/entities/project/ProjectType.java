/*
 * assemble
 * ProjectType.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.entities.project;

import lombok.Getter;

@Getter
public enum ProjectType {
    INTERNAL("Internal"),
    EXTERNAL("External");

    private final String value;

    ProjectType(String value) {
        this.value = value;
    }
}
