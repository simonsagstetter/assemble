package com.assemble.backend.scripts.cmd;

import com.assemble.backend.models.auth.User;
import com.assemble.backend.models.auth.UserRole;
import com.assemble.backend.repositories.auth.UserRepository;
import com.assemble.backend.repositories.auth.UserRoleRepository;
import com.assemble.backend.services.core.IdService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class InitApplicationData implements CommandLineRunner {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IdService idService;

    @Override
    public void run( String... args ) {
        if ( userRoleRepository.count() == 0 ) {
            userRoleRepository.saveAll(
                    List.of(
                            new UserRole( "USER" ),
                            new UserRole( "MANAGER" ),
                            new UserRole( "ADMIN" ),
                            new UserRole( "SUPERADMIN" )
                    )
            );
        }
    }
}
