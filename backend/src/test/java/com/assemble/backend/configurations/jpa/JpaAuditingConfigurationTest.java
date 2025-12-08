package com.assemble.backend.configurations.jpa;

import com.assemble.backend.models.db.EntityGreeting;
import com.assemble.backend.repositories.EntityRepository;
import com.assemble.backend.services.core.IdService;
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

    @Autowired
    private IdService idService;

    @Test
    @DisplayName("JPA Auditing should populate fields")
    void auditing_ShouldWork() {
        EntityGreeting entity = EntityGreeting.builder()
                .id( idService.generateIdFor( EntityGreeting.class ) )
                .message( "Hello Postgres!" )
                .build();

        EntityGreeting actual = entityRepository.save( entity );

        assertThat( actual )
                .extracting( "createdDate", "lastModifiedDate", "createdBy", "lastModifiedBy" )
                .doesNotContainNull();

        assertThat( actual.getCreatedBy().getUsername() ).isEqualTo( "SYSTEM" );
        assertThat( actual.getLastModifiedBy().getUsername() ).isEqualTo( "SYSTEM" );
    }
}