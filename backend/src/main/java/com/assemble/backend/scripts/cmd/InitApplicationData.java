package com.assemble.backend.scripts.cmd;

import com.assemble.backend.models.auth.UserRole;
import com.assemble.backend.repositories.auth.UserRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InitApplicationData implements CommandLineRunner {

    private final UserRoleRepository userRoleRepository;

    public InitApplicationData( UserRoleRepository userRoleRepository ) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void run( String... args ) {
        if ( userRoleRepository.count() == 0 ) {
            userRoleRepository.saveAll(
                    List.of(
                            new UserRole( "ROLE_USER" ),
                            new UserRole( "ROLE_MANAGER" ),
                            new UserRole( "ROLE_ADMIN" ),
                            new UserRole( "ROLE_SUPERADMIN" )
                    )
            );
        }
    }
}
