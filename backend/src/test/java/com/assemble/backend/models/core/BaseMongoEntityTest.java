package com.assemble.backend.models.core;

import com.assemble.backend.models.auth.UserAudit;
import com.assemble.backend.models.db.DocumentGreeting;
import com.assemble.backend.repositories.DocumentRepository;
import com.assemble.backend.services.core.IdService;
import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BaseMongoEntity Integration Test")
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class BaseMongoEntityTest {

    @Autowired
    private IdService idService;

    @MockitoBean
    private AuditorAware<UserAudit> auditorAware;

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    @DisplayName("BaseMongoEntity should populate Id, CreatedDate, CreatedBy, LastModifiedData, LastModifiedBy")
    void baseMongoEntity_ShouldContainPopulatedFields_WhenCreated() {
        //GIVEN
        UserAudit mockedUserAudit = new UserAudit( null, "SYSTEM" );
        Mockito.when( auditorAware.getCurrentAuditor() ).thenReturn( Optional.of( mockedUserAudit ) );

        String testMessage = "Hello MongoDB!";
        String recordId = idService.generateIdFor( DocumentGreeting.class );

        DocumentGreeting given = DocumentGreeting.builder()
                .id( recordId )
                .message( testMessage )
                .build();

        //WHEN
        DocumentGreeting saved = this.documentRepository.save( given );

        DocumentGreeting actual = assertDoesNotThrow( () -> this.documentRepository.findById( saved.getId() ).orElseThrow() );

        //THEN
        assertThat( actual )
                .isNotNull()
                .extracting( "message", "id" )
                .contains( given.getMessage(), given.getId() );

        assertThat( actual )
                .extracting( "createdDate", "lastModifiedDate", "createdBy", "lastModifiedBy" )
                .doesNotContainNull();

        assertEquals( mockedUserAudit, actual.getCreatedBy() );
        assertEquals( mockedUserAudit, actual.getLastModifiedBy() );
    }

    @Test
    @DisplayName("BaseMongoEntity should update LastModifiedDate and LastModifiedBy")
    void baseMongoEntity_ShouldUpdateModifiedFields_WhenUpdated() {
        //GIVEN
        UserAudit createdUserAudit = new UserAudit( null, "SYSTEM" );
        UserAudit lastModifiedUserAudit = new UserAudit( null, "FAKE-USER" );
        Mockito.when( auditorAware.getCurrentAuditor() )
                .thenReturn( Optional.of( createdUserAudit ) )
                .thenReturn( Optional.of( lastModifiedUserAudit ) );

        String testMessage = "Hello MongoDB!";
        String recordId = idService.generateIdFor( DocumentGreeting.class );

        DocumentGreeting given = DocumentGreeting.builder()
                .id( recordId )
                .message( testMessage )
                .build();

        DocumentGreeting created = this.documentRepository.save( given );

        DocumentGreeting stored = assertDoesNotThrow( () -> this.documentRepository.findById( created.getId() ).orElseThrow() );
        stored.setMessage( "Hello again, MongoDB!" );

        //WHEN
        DocumentGreeting actual = assertDoesNotThrow( () -> this.documentRepository.save( stored ) );

        //THEN
        assertThat( created.getCreatedDate().truncatedTo( ChronoUnit.MILLIS ) )
                .isEqualTo( actual.getCreatedDate().truncatedTo( ChronoUnit.MILLIS ) );
        assertEquals( created.getCreatedBy(), actual.getCreatedBy() );
        assertNotEquals( created.getLastModifiedDate(), actual.getLastModifiedDate() );
        assertThat( actual.getLastModifiedBy().getUsername() )
                .isEqualTo( "FAKE-USER" );
    }

}