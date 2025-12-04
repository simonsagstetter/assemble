package com.assemble.backend.configurations.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component(value = "auditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if ( authentication == null || !authentication.isAuthenticated() ) {
            return Optional.of( "SYSTEM" );
        }

        if ( authentication.getPrincipal() instanceof UserDetails userDetails ) {
            return Optional.of( userDetails.getUsername() );
        }

        String username = authentication.getName();

        return Optional.of( username );
    }
}
