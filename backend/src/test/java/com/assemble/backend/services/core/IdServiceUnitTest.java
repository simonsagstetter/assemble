package com.assemble.backend.services.core;

import com.assemble.backend.models.db.EntityGreeting;
import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@Import({ TestcontainersConfiguration.class, IdService.class })
class IdServiceUnitTest {

    @Autowired
    private IdService idService;

    @Test
    void generateIdFor() {
        String id = idService.generateIdFor( EntityGreeting.class );

        // TODO: Test in conjunction with a data model that allows Id Generation

        assertNotNull( id );
        assertTrue( id.startsWith( EntityGreeting.class.getSimpleName().substring( 0, 3 ).toLowerCase() ) );

    }
}