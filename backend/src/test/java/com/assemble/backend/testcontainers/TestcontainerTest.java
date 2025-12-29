/*
 * assemble
 * TestcontainerTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.testcontainers;

import com.assemble.backend.models.entities.db.DocumentGreeting;
import com.assemble.backend.models.entities.db.EntityGreeting;
import com.assemble.backend.repositories.db.DocumentRepository;
import com.assemble.backend.repositories.db.EntityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testcontainers Integration Test")
@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TestcontainerTest {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private EntityRepository entityRepository;

    @Test
    @DisplayName("MongoDB Container should be running and be available for repository interaction")
    void mongoDbContainer_test() {
        String testMessage = "Hello MongoDB!";
        DocumentGreeting given = DocumentGreeting.builder()
                .message( testMessage )
                .build();

        DocumentGreeting saved = this.documentRepository.save( given );

        assert saved.getId() != null;

        DocumentGreeting actual = assertDoesNotThrow( () -> this.documentRepository.findById( saved.getId() ).orElseThrow() );

        assertThat( actual )
                .isNotNull()
                .extracting( "message", "id" )
                .contains( given.getMessage(), saved.getId() );

        assertThat( actual )
                .extracting( "version", "createdDate", "lastModifiedDate", "createdBy", "lastModifiedBy" )
                .doesNotContainNull();

    }

    @Test
    @DisplayName("PostgreSQL Container should be running and be available for repository interaction")
    void postgreSQLContainer_test() {
        String testMessage = "Hello Postgres!";
        EntityGreeting given = EntityGreeting.builder()
                .message( testMessage )
                .build();

        EntityGreeting saved = this.entityRepository.save( given );

        assert saved.getId() != null;

        EntityGreeting actual = assertDoesNotThrow( () -> this.entityRepository.findById( saved.getId() ).orElseThrow() );

        assertThat( actual )
                .isNotNull()
                .extracting( "message", "id" )
                .contains( given.getMessage(), saved.getId() );

        assertThat( actual )
                .extracting( "version", "createdDate", "lastModifiedDate", "createdBy", "lastModifiedBy" )
                .doesNotContainNull();
    }

}
