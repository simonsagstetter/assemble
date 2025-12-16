/*
 * assemble
 * MaritalStatus.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.entities.employee;


import lombok.Getter;

@Getter
public enum MaritalStatus {
    SINGLE( "Single" ),
    MARRIED( "Married Partnership" ),
    CIVIL( "Civil Partnership" ),
    DIVORCED( "Divorced" ),
    WIDOWED( "Widowed" );

    private final String label;

    MaritalStatus( String label ) {
        this.label = label;
    }

}
