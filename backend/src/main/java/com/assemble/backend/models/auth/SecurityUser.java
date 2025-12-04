package com.assemble.backend.models.auth;

import lombok.Getter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class SecurityUser implements UserDetails, CredentialsContainer {

    private User user;

    public SecurityUser( User user ) {
        this.user = user;
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.user.isLocked();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if ( user.getRoles() != null ) {
            return user.getRoles().
                    stream()
                    .map( role -> new SimpleGrantedAuthority( "ROLE_" + role.getName() ) )
                    .toList();
        }
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled();
    }

    @Override
    public void eraseCredentials() {
        this.user.setPassword( null );
    }
}
