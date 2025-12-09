/*
 * assemble
 * AuditorAwareImplTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.configurations.security;

import com.assemble.backend.models.entities.auth.SecurityUser;
import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.auth.UserAudit;
import com.assemble.backend.models.entities.auth.UserRole;
import com.assemble.backend.services.core.IdService;
import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AuditorAwareImpl Integration Test")
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class AuditorAwareImplTest {

    @Autowired
    private IdService idService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Authentication authentication = Mockito.mock( Authentication.class );

    private final SecurityContext securityContext = Mockito.mock( SecurityContext.class );

    @DisplayName("getCurrentAuditor should return SYSTEM user")
    @Test
    void getCurrentAuditor_ShouldReturnSystem() {
        Mockito.when( authentication.isAuthenticated() ).thenReturn( false );
        Mockito.when( securityContext.getAuthentication() ).thenReturn( authentication );

        SecurityContextHolder.setContext( securityContext );

        AuditorAwareImpl auditorAware = new AuditorAwareImpl();
        UserAudit actual = auditorAware.getCurrentAuditor().orElseThrow();

        assertEquals( "SYSTEM", actual.getUsername() );
    }

    @DisplayName("getCurrentAuditor should return username from authentication obj")
    @Test
    void getCurrentAuditor_ShouldReturnAuthenticatedUser() {
        SecurityUser user = new SecurityUser( User.builder()
                .id( idService.generateIdFor( User.class ) )
                .username( "mustermannmax" )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .password( passwordEncoder.encode( "securePassword" ) )
                .email( "max.mustermann@example.com" )
                .roles( List.of( UserRole.USER ) )
                .build()
        );
        UserAudit expected = new UserAudit( user.getUser().getId(), user.getUsername() );

        Mockito.when( authentication.isAuthenticated() ).thenReturn( true );
        Mockito.when( authentication.getName() ).thenReturn( "mustermannmax" );
        Mockito.when( authentication.getPrincipal() ).thenReturn( user );
        Mockito.when( securityContext.getAuthentication() ).thenReturn( authentication );

        SecurityContextHolder.setContext( securityContext );

        AuditorAwareImpl auditorAware = new AuditorAwareImpl();
        UserAudit actual = auditorAware.getCurrentAuditor().orElseThrow();

        assertEquals( expected.getId(), actual.getId() );
        assertEquals( expected.getUsername(), actual.getUsername() );
    }

    @DisplayName("getCurrentAuditor should return username from custom userdetails obj")
    @Test
    void getCurrentAuditor_ShouldReturnAuthenticatedUser_FromUserDetails() {
        Mockito.when( authentication.isAuthenticated() ).thenReturn( true );

        SecurityUser user = new SecurityUser(
                User.builder()
                        .id( idService.generateIdFor( User.class ) )
                        .username( "mustermannmax" )
                        .firstname( "Max" )
                        .lastname( "Mustermann" )
                        .password( passwordEncoder.encode( "securePassword" ) )
                        .email( "max.mustermann@exmaple.com" )
                        .roles( List.of() )
                        .build()
        );

        UserAudit expected = new UserAudit( user.getUser().getId(), user.getUsername() );

        Mockito.when( authentication.getPrincipal() ).thenReturn( user );
        Mockito.when( securityContext.getAuthentication() ).thenReturn( authentication );

        SecurityContextHolder.setContext( securityContext );

        AuditorAwareImpl auditorAware = new AuditorAwareImpl();
        UserAudit actual = auditorAware.getCurrentAuditor().orElseThrow();

        assertEquals( expected.getId(), actual.getId() );
        assertEquals( expected.getUsername(), actual.getUsername() );
    }
}