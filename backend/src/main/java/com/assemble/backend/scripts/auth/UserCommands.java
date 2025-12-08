package com.assemble.backend.scripts.auth;

import com.assemble.backend.models.auth.User;
import com.assemble.backend.models.auth.UserRole;
import com.assemble.backend.repositories.auth.UserRepository;
import com.assemble.backend.services.core.IdService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;
import java.util.UUID;

@Slf4j
@ShellComponent
@AllArgsConstructor
public class UserCommands {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final IdService idService;

    @ShellMethod(
            key = "create-superuser"
    )
    public String createSuperUser() {
        if ( userRepository.countDistinctByRolesContaining( UserRole.SUPERUSER ) == 0 ) {
            String id = idService.generateIdFor( User.class );
            String rawPassword = UUID.randomUUID().toString().replace( "-", "" );
            User superUser = User.builder()
                    .id( id )
                    .username( "superuser" )
                    .email( "test@example.com" )
                    .password( passwordEncoder.encode( rawPassword ) )
                    .roles( List.of( UserRole.SUPERUSER ) )
                    .build();

            userRepository.save( superUser );

            log.info( superUser.toString() );

            return "Superuser created with username: superuser and password: " + rawPassword;
        }
        return "Superuser already exists!";
    }

}
