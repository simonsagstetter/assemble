/*
 * assemble
 * WithMockCustomUserSecurityContextFactory.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.testutils;

import com.assemble.backend.models.entities.auth.SecurityUser;
import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.repositories.auth.UserRepository;
import com.github.f4b6a3.uuid.UuidCreator;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;

@AllArgsConstructor
final class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private UserRepository userRepository;

    @Override
    public SecurityContext createSecurityContext( WithMockCustomUser customUser ) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        User user = null;
        User defaultUser = User.builder()
                .username( customUser.username() )
                .email( customUser.email() )
                .firstname( customUser.firstname() )
                .lastname( customUser.lastname() )
                .roles( Arrays.stream( customUser.roles() ).toList() )
                .password( PASSWORD_ENCODER.encode( "secret" ) )
                .build();

        if ( customUser.saveToDatabase() ) {
            user = userRepository.save( defaultUser );
        } else {
            defaultUser.setId( UuidCreator.getTimeOrderedEpoch() );
            user = defaultUser;
        }

        SecurityUser principal = new SecurityUser(
                user
        );

        Authentication auth = UsernamePasswordAuthenticationToken.authenticated(
                principal,
                principal.getPassword(),
                principal.getAuthorities()
        );

        context.setAuthentication( auth );

        return context;
    }
}
