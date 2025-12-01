package com.assemble.backend.controllers.rest;

import com.assemble.backend.models.db.EntityGreeting;
import com.assemble.backend.repositories.EntityRepository;
import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class GreetingRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityRepository entityRepository;

    @Test
    void testGetAllGreetings() throws Exception {
        EntityGreeting data = this.entityRepository.save(
                new EntityGreeting( null, "Hello World!" )
        );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get( "/api/greetings" )
                                .accept( MediaType.APPLICATION_JSON_VALUE )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$[0].message" ).value( data.getMessage() )
                );

    }
}