/*
 * assemble
 * JpaAuditingConfigurationTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.configurations.jpa;

import com.assemble.backend.models.entities.db.EntityGreeting;
import com.assemble.backend.repositories.db.EntityRepository;
import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JpaAuditingConfiguration Integration Test")
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class JpaAuditingConfigurationTest {

    @Autowired
    private EntityRepository entityRepository;

    @Test
    @DisplayName("JPA Auditing should populate fields")
    void auditing_ShouldWork() {
        EntityGreeting entity = EntityGreeting.builder()
                .message( "Hello Postgres!" )
                .build();

        EntityGreeting actual = entityRepository.save( entity );

        assertThat( actual )
                .extracting( "version", "createdDate", "lastModifiedDate", "createdBy", "lastModifiedBy" )
                .doesNotContainNull();

        assertThat( actual.getCreatedBy().getUsername() ).isEqualTo( "SYSTEM" );
        assertThat( actual.getLastModifiedBy().getUsername() ).isEqualTo( "SYSTEM" );
    }
}