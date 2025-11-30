package com.assemble.backend.operations;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.TestcontainersConfiguration;

@Testcontainers
@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureGraphQlTester
class GreetingOperationTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    void testGreetingOperation() {
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
                .isEqualTo( "Hello, world!" );
    }
}