package com.assemble.backend.services.auth;

import com.assemble.backend.models.auth.SecurityUser;
import com.assemble.backend.models.auth.User;
import com.assemble.backend.repositories.auth.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService( UserRepository userRepository ) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
        User user = userRepository.findByUsername( username ).orElseThrow(
                () -> new UsernameNotFoundException( "User not found with username: " + username )
        );

        return new SecurityUser( user );
    }
}
