package com.assemble.backend.configurations.security;

import com.assemble.backend.models.auth.SecurityUser;
import com.assemble.backend.models.auth.User;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

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
        Optional<String> actual = auditorAware.getCurrentAuditor();

        assertEquals( Optional.of( "SYSTEM" ), actual );
    }

    @DisplayName("getCurrentAuditor should return username from authentication obj")
    @Test
    void getCurrentAuditor_ShouldReturnAuthenticatedUser() {
        Mockito.when( authentication.isAuthenticated() ).thenReturn( true );
        Mockito.when( authentication.getName() ).thenReturn( "mustermannmax" );
        Mockito.when( securityContext.getAuthentication() ).thenReturn( authentication );

        SecurityContextHolder.setContext( securityContext );

        AuditorAwareImpl auditorAware = new AuditorAwareImpl();
        Optional<String> actual = auditorAware.getCurrentAuditor();

        assertEquals( Optional.of( "mustermannmax" ), actual );
    }

    @DisplayName("getCurrentAuditor should return username from custom userdetails obj")
    @Test
    void getCurrentAuditor_ShouldReturnAuthenticatedUser_FromUserDetails() {
        Mockito.when( authentication.isAuthenticated() ).thenReturn( true );

        User user = User.builder()
                .id( idService.generateIdFor( User.class ) )
                .username( "mustermannmax" )
                .password( passwordEncoder.encode( "securePassword" ) )
                .email( "max.mustermann@exmaple.com" )
                .roles( List.of() )
                .build();

        UserDetails userDetails = new SecurityUser( user );

        Mockito.when( authentication.getPrincipal() ).thenReturn( userDetails );
        Mockito.when( securityContext.getAuthentication() ).thenReturn( authentication );

        SecurityContextHolder.setContext( securityContext );

        AuditorAwareImpl auditorAware = new AuditorAwareImpl();
        Optional<String> actual = auditorAware.getCurrentAuditor();

        assertEquals( Optional.of( userDetails.getUsername() ), actual );
    }
}