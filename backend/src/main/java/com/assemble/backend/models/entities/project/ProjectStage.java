/*
 * assemble
 * ProjectStage.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.entities.project;

import lombok.Getter;

@Getter
public enum ProjectStage {
    PROPOSAL("Proposal"),
    NEGOTIATION("Negotiation"),
    ASSIGNED("Assigned"),
    IMPLEMENTATION("Implementation"),
    FINAL_BILLING("Final Billing"),
    COMPLETED("Completed"),
    CLOSED("Closed");

    private final String value;

    ProjectStage(String value) {
        this.value = value;
    }
}
