/*
 * assemble
 * BaseJPAEntityTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.entities.core;

import com.assemble.backend.models.entities.auth.UserAudit;
import com.assemble.backend.models.entities.db.EntityGreeting;
import com.assemble.backend.repositories.db.EntityRepository;
import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BaseJPAEntity Integration Test")
@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BaseJPAEntityTest {

    @Autowired
    private EntityRepository entityRepository;

    @MockitoBean
    private AuditorAware<UserAudit> auditorAware;

    @Test
    @DisplayName("BaseJPAEntity should populate Id, CreatedDate, CreatedBy, LastModifiedData, LastModifiedBy")
    void baseJpaEntity_ShouldContainPopulatedFields_WhenCreated() {
        //GIVEN
        UserAudit mockedUserAudit = new UserAudit( null, "SYSTEM" );
        Mockito.when( auditorAware.getCurrentAuditor() ).thenReturn( Optional.of( mockedUserAudit ) );

        String testMessage = "Hello Postgres!";

        EntityGreeting given = EntityGreeting.builder()
                .message( testMessage )
                .build();

        //WHEN
        EntityGreeting saved = this.entityRepository.save( given );

        assert saved.getId() != null;

        EntityGreeting actual = assertDoesNotThrow( () -> this.entityRepository.findById( saved.getId() ).orElseThrow() );

        //THEN
        assertThat( actual )
                .isNotNull()
                .extracting( "message", "id" )
                .contains( given.getMessage(), saved.getId() );

        assertThat( actual )
                .extracting( "version", "createdDate", "lastModifiedDate", "createdBy", "lastModifiedBy" )
                .doesNotContainNull();

        assertEquals( mockedUserAudit, actual.getCreatedBy() );
        assertEquals( mockedUserAudit, actual.getLastModifiedBy() );
    }

    @Test
    @DisplayName("BaseJPAEntity should update LastModifiedDate and LastModifiedBy")
    void baseJpaEntity_ShouldUpdateModifiedFields_WhenUpdated() {
        //GIVEN
        UserAudit createdUserAudit = new UserAudit( null, "SYSTEM" );
        UserAudit lastModifiedUserAudit = new UserAudit( null, "FAKE-USER" );

        Mockito.when( auditorAware.getCurrentAuditor() )
                .thenReturn( Optional.of( createdUserAudit ) )
                .thenReturn( Optional.of( lastModifiedUserAudit ) );

        String testMessage = "Hello Postgres!";

        EntityGreeting given = EntityGreeting.builder()
                .message( testMessage )
                .build();

        EntityGreeting created = this.entityRepository.save( given );

        assert created.getId() != null;

        EntityGreeting stored = assertDoesNotThrow( () -> this.entityRepository.findById( created.getId() ).orElseThrow() );
        stored.setMessage( "Hello again, Postgres!" );

        //WHEN
        EntityGreeting actual = assertDoesNotThrow( () -> this.entityRepository.save( stored ) );

        //THEN
        assertThat( created.getCreatedDate().truncatedTo( ChronoUnit.MILLIS ) )
                .isEqualTo( actual.getCreatedDate().truncatedTo( ChronoUnit.MILLIS ) );
        assertEquals( created.getCreatedBy(), actual.getCreatedBy() );
        assertNotEquals( created.getVersion(), actual.getVersion() );
        assertNotEquals( created.getLastModifiedDate(), actual.getLastModifiedDate() );
        assertThat( actual.getLastModifiedBy().getUsername() )
                .isEqualTo( "FAKE-USER" );

    }
}