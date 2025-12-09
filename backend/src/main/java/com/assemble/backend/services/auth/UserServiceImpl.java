/*
 * assemble
 * UserServiceImpl.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.auth;

import com.assemble.backend.exceptions.auth.PasswordMismatchException;
import com.assemble.backend.models.dtos.auth.UserDTO;
import com.assemble.backend.models.entities.auth.SecurityUser;
import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.mappers.auth.UserMapper;
import com.assemble.backend.repositories.auth.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void changePassword( String username, String oldPassword, String newPassword ) {
        User user = userRepository.findByUsername( username )
                .orElseThrow( () -> new EntityNotFoundException( "User not found" ) );

        if ( !passwordEncoder.matches( oldPassword, user.getPassword() ) ) {
            throw new PasswordMismatchException( "Old password does not match" );
        }

        user.setPassword( passwordEncoder.encode( newPassword ) );
        this.userRepository.save( user );
    }

    @Override
    public UserDTO findMe( SecurityUser securityUser ) {
        return userMapper.toUserDTO(
                userRepository.findByUsername( securityUser.getUsername() )
                        .orElseThrow( () -> new EntityNotFoundException( "User not found" ) )
        );
    }

}
