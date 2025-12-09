/*
 * assemble
 * IdServiceUnitTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("IdService Unit Test")
class IdServiceUnitTest {

    private final IdService idService = new IdService();

    @Test
    @DisplayName("IdService.generateIdFor() should return uuid with first and last char of class simple name")
    void generateIdFor_ShouldReturnValidId_WhenCalledWithClass() {
        String expected = "sg";
        String actual = idService.generateIdFor( String.class );
        assertTrue( actual.startsWith( expected ) );
    }
}
