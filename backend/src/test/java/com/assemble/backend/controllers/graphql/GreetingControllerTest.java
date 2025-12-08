package com.assemble.backend.controllers.graphql;

import com.assemble.backend.services.core.IdService;
import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import com.assemble.backend.models.db.DocumentGreeting;
import com.assemble.backend.repositories.DocumentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

@DisplayName("Graphql GreetingController Integration Test")
@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureGraphQlTester
class GreetingControllerTest {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private IdService idService;

    @Autowired
    private GraphQlTester graphQlTester;


    @Test
    @DisplayName("GET /greetings should return all greeting")
    void getAllGreetings_shouldReturnGreetings() {
        DocumentGreeting data = this.documentRepository.insert(
                DocumentGreeting.builder()
                        .id( idService.generateIdFor( DocumentGreeting.class ) )
                        .message( "Hello MongoDB!" )
                        .build()
        );

        String query = """ 
                    query GetAllGreetings {
                        greetings {
                            id,
                            message
                        }
                    }
                """;

        graphQlTester
                .document( query )
                .execute()
                .path( "greetings[0].id" )
                .entity( String.class )
                .isEqualTo( data.getId() )
                .path( "greetings[0].message" )
                .entity( String.class )
                .isEqualTo( data.getMessage() );

    }
}