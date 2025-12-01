package com.assemble.backend.controllers.graphql;

import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import com.assemble.backend.models.db.DocumentGreeting;
import com.assemble.backend.repositories.DocumentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureGraphQlTester
class GreetingControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    void testGetAllGreetings() {
        DocumentGreeting data = this.documentRepository.insert(
                new DocumentGreeting( null, "Hello World!" )
        );


        graphQlTester.document( """
                        query GetAllGreetings {
                           greetings {
                              message
                           }
                        }
                        """ )
                .execute()
                .path( "greetings[0].message" )
                .entity( String.class )
                .isEqualTo( data.message() );
    }
}