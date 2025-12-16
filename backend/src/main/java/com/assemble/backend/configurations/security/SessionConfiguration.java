/*
 * assemble
 * SessionConfiguration.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.configurations.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

@Configuration
public class SessionConfiguration {

    @Bean
    public SessionRegistry sessionRegistry(
            FindByIndexNameSessionRepository<? extends Session> sessionRepository
    ) {
        return new SpringSessionBackedSessionRegistry<>( sessionRepository );
    }
}
