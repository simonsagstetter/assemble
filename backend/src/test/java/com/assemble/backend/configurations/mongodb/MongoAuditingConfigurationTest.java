/*
 * assemble
 * MongoAuditingConfigurationTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.configurations.mongodb;

import com.assemble.backend.models.entities.db.DocumentGreeting;
import com.assemble.backend.repositories.db.DocumentRepository;
import com.assemble.backend.services.core.IdService;
import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MongoAuditiongConfiguration Integration Test")
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class MongoAuditingConfigurationTest {
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private IdService idService;

    @Test
    @DisplayName("MongoDB Auditing should populate fields")
    void auditing_ShouldWork() {
        DocumentGreeting entity = DocumentGreeting.builder()
                .id( idService.generateIdFor( DocumentGreeting.class ) )
                .message( "Hello Postgres!" )
                .build();

        DocumentGreeting actual = documentRepository.save( entity );

        assertThat( actual )
                .extracting( "version", "createdDate", "lastModifiedDate", "createdBy", "lastModifiedBy" )
                .doesNotContainNull();

        assertThat( actual.getCreatedBy().getUsername() ).isEqualTo( "SYSTEM" );
        assertThat( actual.getLastModifiedBy().getUsername() ).isEqualTo( "SYSTEM" );
    }
}