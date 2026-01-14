/*
 * assemble
 * UserBootstrapTest.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.bootstrap.auth;

import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.repositories.auth.UserRepository;
import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "assemble.env=prod")
@DisplayName("UserBootstrap Integration Test")
@Import(TestcontainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserBootstrapTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("UserRepository should contain one superuser record")
    void userRepository_ShouldContainOneSuperUserRecord() {
        List<User> users = userRepository.findAll();

        assertEquals( 1, users.size() );

        assertTrue( users.getFirst().getUsername().equals( "admin" ) );
    }

}