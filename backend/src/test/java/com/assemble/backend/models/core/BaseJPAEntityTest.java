package com.assemble.backend.models.core;

import com.assemble.backend.models.db.EntityGreeting;
import com.assemble.backend.repositories.EntityRepository;
import com.assemble.backend.services.core.IdService;
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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BaseJPAEntity Integration Test")
@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BaseJPAEntityTest {

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private IdService idService;

    @MockitoBean
    private AuditorAware<String> auditorAware;

    @Test
    @DisplayName("BaseJPAEntity should populate Id, CreatedDate, CreatedBy, LastModifiedData, LastModifiedBy")
    void baseJpaEntity_ShouldContainPopulatedFields_WhenCreated() {
        //GIVEN
        Mockito.when( auditorAware.getCurrentAuditor() ).thenReturn( Optional.of( "SYSTEM" ) );

        String testMessage = "Hello Postgres!";
        String recordId = idService.generateIdFor( EntityGreeting.class );

        EntityGreeting given = EntityGreeting.builder()
                .id( recordId )
                .message( testMessage )
                .build();

        //WHEN
        EntityGreeting saved = this.entityRepository.save( given );

        EntityGreeting actual = assertDoesNotThrow( () -> this.entityRepository.findById( saved.getId() ).orElseThrow() );

        //THEN
        assertThat( actual )
                .isNotNull()
                .extracting( "message", "id" )
                .contains( given.getMessage(), given.getId() );

        assertThat( actual )
                .extracting( "createdDate", "lastModifiedDate", "createdBy", "lastModifiedBy" )
                .doesNotContainNull();
    }

    @Test
    @DisplayName("BaseJPAEntity should update LastModifiedDate and LastModifiedBy")
    void baseJpaEntity_ShouldUpdateModifiedFields_WhenUpdated() {
        //GIVEN
        Mockito.when( auditorAware.getCurrentAuditor() )
                .thenReturn( Optional.of( "SYSTEM" ) )
                .thenReturn( Optional.of( "FAKE-USER" ) );

        String testMessage = "Hello Postgres!";
        String recordId = idService.generateIdFor( EntityGreeting.class );

        EntityGreeting given = EntityGreeting.builder()
                .id( recordId )
                .message( testMessage )
                .build();

        EntityGreeting created = this.entityRepository.save( given );

        EntityGreeting stored = assertDoesNotThrow( () -> this.entityRepository.findById( created.getId() ).orElseThrow() );
        stored.setMessage( "Hello again, Postgres!" );

        //THEN
        EntityGreeting actual = assertDoesNotThrow( () -> this.entityRepository.save( stored ) );

        //ASSERT
        Instant createdDate = created.getCreatedDate().orElseThrow();
        Instant actualCreatedDate = actual.getCreatedDate().orElseThrow();

        assertThat( actualCreatedDate.truncatedTo( ChronoUnit.MILLIS ) )
                .isEqualTo( createdDate.truncatedTo( ChronoUnit.MILLIS ) );

        assertThat( actual.getCreatedBy() )
                .isEqualTo( created.getCreatedBy() );

        assertThat( actual.getLastModifiedDate() )
                .isNotEqualTo( created.getLastModifiedDate() );

        assertThat( actual.getLastModifiedBy() )
                .isEqualTo( Optional.of( "FAKE-USER" ) );
    }
}