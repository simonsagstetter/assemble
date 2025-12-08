package com.assemble.backend.scripts.auth;

import com.assemble.backend.models.auth.User;
import com.assemble.backend.models.auth.UserRole;
import com.assemble.backend.repositories.auth.UserRepository;
import com.assemble.backend.services.core.IdService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;
import java.util.UUID;

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
            String rawPassword = UUID.randomUUID().toString().replaceAll( "-", "" );
            User superUser = User.builder()
                    .id( id )
                    .username( "superuser" )
                    .email( "test@example.com" )
                    .password( passwordEncoder.encode( rawPassword ) )
                    .roles( List.of( UserRole.SUPERUSER ) )
                    .build();

            userRepository.save( superUser );

            System.out.println( "Superuser created with username: superuser and password: " + rawPassword );

            return "Superuser created!";
        }
        return "Superuser already exists!";
    }

}
