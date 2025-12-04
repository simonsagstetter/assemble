package com.assemble.backend.testcontainers;

import com.assemble.backend.models.db.DocumentGreeting;
import com.assemble.backend.models.db.EntityGreeting;
import com.assemble.backend.repositories.DocumentRepository;
import com.assemble.backend.repositories.EntityRepository;
import com.assemble.backend.services.core.IdService;
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

    @Autowired
    private IdService idService;

    @Test
    @DisplayName("MongoDB Container should be running and be available for repository interaction")
    void mongoDbContainer_test() {
        String testMessage = "Hello MongoDB!";
        String recordId = idService.generateIdFor( DocumentGreeting.class );

        DocumentGreeting given = DocumentGreeting.builder()
                .id( recordId )
                .message( testMessage )
                .build();

        DocumentGreeting saved = this.documentRepository.save( given );

        DocumentGreeting actual = assertDoesNotThrow( () -> this.documentRepository.findById( saved.getId() ).orElseThrow() );

        assertThat( actual )
                .isNotNull()
                .extracting( "message", "id" )
                .contains( given.getMessage(), given.getId() );

        assertThat( actual )
                .extracting( "createdDate", "lastModifiedDate", "createdBy", "lastModifiedBy" )
                .doesNotContainNull();

    }

    @Test
    @DisplayName("PostgreSQL Container should be running and be available for repository interaction")
    void postgreSQLContainer_test() {
        String testMessage = "Hello Postgres!";
        String recordId = idService.generateIdFor( EntityGreeting.class );
        EntityGreeting given = EntityGreeting.builder()
                .id( recordId )
                .message( testMessage )
                .build();

        EntityGreeting saved = this.entityRepository.save( given );

        EntityGreeting actual = assertDoesNotThrow( () -> this.entityRepository.findById( saved.getId() ).orElseThrow() );

        assertThat( actual )
                .isNotNull()
                .extracting( "message", "id" )
                .contains( given.getMessage(), given.getId() );

        assertThat( actual )
                .extracting( "createdDate", "lastModifiedDate", "createdBy", "lastModifiedBy" )
                .doesNotContainNull();
    }

}
