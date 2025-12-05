package com.assemble.backend.controllers.rest;

import com.assemble.backend.models.db.EntityGreeting;
import com.assemble.backend.repositories.EntityRepository;
import com.assemble.backend.services.core.IdService;
import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@DisplayName("Rest GreetingController Integration Test")
@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class GreetingRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private IdService idService;

    @Test
    void getAllGreetings_ShouldReturnAllGreetings() throws Exception {
        EntityGreeting data = this.entityRepository.save(
                EntityGreeting.builder()
                        .id( idService.generateIdFor( EntityGreeting.class ) )
                        .message( "Hello World!" )
                        .build()
        );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get( "/api/greetings" )
                                .accept( MediaType.APPLICATION_JSON_VALUE )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$[0].id" ).value( data.getId() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$[0].message" ).value( data.getMessage() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$[0].createdDate" ).isNotEmpty()
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$[0].createdBy" ).value( data.getCreatedBy() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$[0].lastModifiedDate" ).isNotEmpty()
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$[0].lastModifiedBy" ).value( data.getLastModifiedBy() )
                );

    }
}