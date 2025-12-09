/*
 * assemble
 * TestBackendApplication.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend;

import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import org.springframework.boot.SpringApplication;

public class TestBackendApplication {

    public static void main( String[] args ) {
        SpringApplication.from( BackendApplication::main ).with( TestcontainersConfiguration.class ).run( args );
    }

}
