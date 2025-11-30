package com.assemble.backend;

import com.assemble.backend.models.db.DocumentGreeting;
import com.assemble.backend.models.db.EntityGreeting;
import com.assemble.backend.repositories.DocumentRepository;
import com.assemble.backend.repositories.EntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TestcontainerTest {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private EntityRepository entityRepository;

    private static final String testMessage = "Hello World!";

    @Test
    void testDocumentRepository() {

        DocumentGreeting given = this.documentRepository.insert(
                new DocumentGreeting( null, testMessage )
        );

        DocumentGreeting actual = this.documentRepository.findById( given.id() )
                .orElse( null );

        assertThat( actual )
                .isNotNull()
                .extracting( "message" )
                .isEqualTo( testMessage );
    }

    @Test
    void testEntityRepository() {
        EntityGreeting given = this.entityRepository.save(
                new EntityGreeting( null, testMessage )
        );

        EntityGreeting actual = this.entityRepository.findById( given.getId() )
                .orElse( null );

        assertThat( actual )
                .isNotNull()
                .extracting( "message" )
                .isEqualTo( testMessage );
    }

}
