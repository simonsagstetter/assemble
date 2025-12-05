package com.assemble.backend.models.core;

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
    private AuditorAware<String> auditorAware;

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    @DisplayName("BaseMongoEntity should populate Id, CreatedDate, CreatedBy, LastModifiedData, LastModifiedBy")
    void baseMongoEntity_ShouldContainPopulatedFields_WhenCreated() {
        //GIVEN
        Mockito.when( auditorAware.getCurrentAuditor() ).thenReturn( Optional.of( "SYSTEM" ) );

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
    }

    @Test
    @DisplayName("BaseMongoEntity should update LastModifiedDate and LastModifiedBy")
    void baseMongoEntity_ShouldUpdateModifiedFields_WhenUpdated() {
        //GIVEN
        Mockito.when( auditorAware.getCurrentAuditor() )
                .thenReturn( Optional.of( "SYSTEM" ) )
                .thenReturn( Optional.of( "FAKE-USER" ) );

        String testMessage = "Hello MongoDB!";
        String recordId = idService.generateIdFor( DocumentGreeting.class );

        DocumentGreeting given = DocumentGreeting.builder()
                .id( recordId )
                .message( testMessage )
                .build();

        DocumentGreeting created = this.documentRepository.save( given );

        DocumentGreeting stored = assertDoesNotThrow( () -> this.documentRepository.findById( created.getId() ).orElseThrow() );
        stored.setMessage( "Hello again, MongoDB!" );

        //THEN
        DocumentGreeting actual = assertDoesNotThrow( () -> this.documentRepository.save( stored ) );

        //ASSERT
        assertThat( created.getCreatedDate().truncatedTo( ChronoUnit.MILLIS ) )
                .isEqualTo( actual.getCreatedDate().truncatedTo( ChronoUnit.MILLIS ) );

        assertThat( actual.getCreatedBy() )
                .isEqualTo( created.getCreatedBy() );
        assertThat( actual.getLastModifiedDate() )
                .isNotEqualTo( created.getLastModifiedDate() );

        assertThat( actual.getLastModifiedBy() )
                .isEqualTo( "FAKE-USER" );
    }

}