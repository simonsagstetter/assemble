package com.assemble.backend.configurations.security;

import com.assemble.backend.models.auth.SecurityUser;
import com.assemble.backend.models.auth.UserAudit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component(value = "auditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware<UserAudit> {

    @Override
    public Optional<UserAudit> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if ( authentication != null &&
                authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof SecurityUser principal
        ) {
            return Optional.of(
                    new UserAudit(
                            principal.getUser().getId(),
                            principal.getUsername()
                    )
            );
        }

        return Optional.of(
                new UserAudit( null, "SYSTEM" )
        );
    }
}
