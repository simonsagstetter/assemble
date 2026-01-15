/*
 * assemble
 * AppSettingsBootstrapTest.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.bootstrap.app;

import com.assemble.backend.repositories.app.AppSettingsRepository;
import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("AppSettingsBootstrap Integration Test")
class AppSettingsBootstrapTest {

    @Autowired
    private AppSettingsRepository appSettingsRepository;

    @Test
    @DisplayName("AppSettingsRepository should contain one AppSettings record")
    void appSettingsRepository_ShouldContainOneRecord() {
        long count = appSettingsRepository.count();
        assertEquals( 1, count );
    }
}