/*
 * assemble
 * CustomUserDetailsServiceUnitTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.auth;

import com.assemble.backend.models.entities.auth.SecurityUser;
import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.auth.UserRole;
import com.assemble.backend.repositories.auth.UserRepository;
import com.assemble.backend.services.core.IdService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CustomUserDetailsService Unit Test")
@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final IdService idService = new IdService();

    @DisplayName("loadUserByUsername should return an instance of UserDetails with all Details")
    @Test
    void loadUserByUsername_ShouldReturnUserDetails() {
        Collection<GrantedAuthority> authorities = List.of( new SimpleGrantedAuthority( "ROLE_" + UserRole.USER.name() ) );

        User givenUser = User.builder()
                .id( idService.generateIdFor( User.class ) )
                .username( "mustermannmax" )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .password( passwordEncoder.encode( "CompletelySecurePassword123!" ) )
                .email( "max.mustermann@example.com" )
                .roles( List.of( UserRole.USER ) )
                .enabled( true )
                .build();

        Mockito.when( userRepository.findByUsername( givenUser.getUsername() ) )
                .thenReturn( Optional.of( givenUser ) );

        UserDetails userDetails = assertDoesNotThrow( () -> customUserDetailsService.loadUserByUsername( givenUser.getUsername() ) );
        SecurityUser securityUser = ( SecurityUser ) userDetails;

        assertThat( userDetails )
                .isNotNull()
                .isInstanceOf( SecurityUser.class )
                .extracting( "user" )
                .isEqualTo( givenUser );

        assertEquals( authorities, userDetails.getAuthorities() );
        assertEquals( givenUser.getFirstname() + " " + givenUser.getLastname(), securityUser.getUser().getFullname() );
        assertEquals( givenUser.getUsername(), userDetails.getUsername() );
        assertEquals( givenUser.getPassword(), userDetails.getPassword() );
        assertTrue( userDetails.isAccountNonExpired() );
        assertTrue( userDetails.isAccountNonLocked() );
        assertTrue( userDetails.isCredentialsNonExpired() );
        assertTrue( userDetails.isEnabled() );
    }

    @DisplayName("loadUserByUsername should return an instance of SecurityUser with all Details of User")
    @Test
    void loadUserByUsername_ShouldThrowUserNameNotFoundException_WhenUserNotFound() {

        Mockito.when( userRepository.findByUsername( "not-existing-username" ) )
                .thenReturn( Optional.empty() );

        UsernameNotFoundException ex = assertThrows( UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername( "not-existing-username" ) );

        assertThat( ex )
                .isNotNull()
                .isInstanceOf( UsernameNotFoundException.class );
    }

}