package com.assemble.backend.configurations.mongodb;

import com.assemble.backend.models.db.DocumentGreeting;
import com.assemble.backend.repositories.DocumentRepository;
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
                .extracting( "createdDate", "lastModifiedDate", "createdBy", "lastModifiedBy" )
                .doesNotContainNull();

        assertThat( actual.getCreatedBy() ).isEqualTo( "SYSTEM" );
        assertThat( actual.getLastModifiedBy() ).isEqualTo( "SYSTEM" );
    }
}