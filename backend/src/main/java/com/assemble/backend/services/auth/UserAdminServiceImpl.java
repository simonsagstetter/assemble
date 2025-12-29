/*
 * assemble
 * UserAdminServiceImpl.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.auth;

import com.assemble.backend.models.dtos.auth.admin.*;
import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.auth.UserRole;
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.models.mappers.auth.UserAdminMapper;
import com.assemble.backend.repositories.auth.UserRepository;
import com.assemble.backend.repositories.employee.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {

    private final UserRepository userRepository;
    private final UserAdminMapper userAdminMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final SessionService sessionService;

    @Override
    public List<UserAdminDTO> getAllUsers() {
        return this.userRepository.findAll()
                .stream()
                .map( userAdminMapper::toUserAdminDTO )
                .toList();
    }

    @Override
    public UserAdminDTO getUserById( String id ) {
        return userAdminMapper.toUserAdminDTO(
                userRepository.findById( UUID.fromString( id ) )
                        .orElseThrow(
                                () -> new EntityNotFoundException( "User not found with id: " + id )
                        )
        );
    }


    @Override
    @Transactional(readOnly = true)
    public List<UserRefDTO> searchUnlinkedUsers( String searchTerm ) {
        String normalizedSearchTerm = searchTerm == null ? "" : searchTerm.toLowerCase();
        return userRepository
                .search( normalizedSearchTerm )
                .stream()
                .map( userAdminMapper::userToUserRefDTO )
                .toList();
    }

    @Override
    @Transactional
    public UserAdminDTO setUserStatus( String id, UserUpdateStatusDTO userUpdateStatusDTO ) {
        User user = userRepository.findById( UUID.fromString( id ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find user with id: " + id )
                );

        if ( !userUpdateStatusDTO.getEnabled() || userUpdateStatusDTO.getLocked() ) {
            sessionService.invalidateUserSessions( user.getUsername() );
        }

        user.setEnabled( userUpdateStatusDTO.getEnabled() );
        user.setLocked( userUpdateStatusDTO.getLocked() );

        return userAdminMapper.toUserAdminDTO( userRepository.save( user ) );
    }

    @Override
    @Transactional
    public UserAdminDTO setUserRoles( String id, List<UserRole> roles ) {
        User user = userRepository.findById( UUID.fromString( id ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find user with id: " + id )
                );

        user.setRoles( roles );
        return userAdminMapper.toUserAdminDTO( userRepository.save( user ) );
    }

    @Override
    @Transactional
    public UserAdminDTO setUserEmployee( String userId, UserUpdateEmployeeDTO userUpdateEmployeeDTO ) {
        User user = userRepository.findById( UUID.fromString( userId ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find user with id: " + userId )
                );

        Employee employee = userUpdateEmployeeDTO.getEmployeeId() != null ? employeeRepository
                .findById( UUID.fromString( userUpdateEmployeeDTO.getEmployeeId() ) )
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Could not find employee with id: " + userUpdateEmployeeDTO.getEmployeeId()
                        )
                )
                : null;

        if ( employee == null && user.getEmployee() != null ) {
            Employee existingEmployee = user.getEmployee();
            existingEmployee.setUser( null );
            user.setEmployee( null );
            employeeRepository.save( existingEmployee );

        } else if ( employee != null ) {
            if ( employee.getUser() != null ) throw new InvalidParameterException(
                    "Employee is already linked to username " + employee.getUser().getUsername()
            );
            Employee currentEmployee = user.getEmployee();
            if ( currentEmployee != null ) {
                currentEmployee.setUser( null );
                employeeRepository.save( currentEmployee );
            }

            employee.setUser( user );
            employeeRepository.save( employee );
            user.setEmployee( employee );
        }

        return userAdminMapper.toUserAdminDTO( user );
    }

    @Override
    @Transactional
    public UserAdminDTO createUser( UserCreateDTO userCreateDTO ) {
        Employee employee = userCreateDTO.getEmployeeId() != null ? employeeRepository
                .findById( UUID.fromString( userCreateDTO.getEmployeeId() ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find employee with id: " + userCreateDTO.getEmployeeId() )
                ) : null;

        if ( employee != null && employee.getUser() != null ) {
            throw new InvalidParameterException( "User is already linked to employee: " + employee.getUser().getUsername() );
        }

        String password = passwordEncoder.encode(
                userCreateDTO.getPassword() != null ?
                        userCreateDTO.getPassword()
                        : UUID.randomUUID()
                        .toString()
                        .replace( "-", "" )
        );

        userCreateDTO.setPassword( password );

        User user = userAdminMapper.toUser( userCreateDTO );
        User savedUser = userRepository.save( user );


        if ( employee != null ) {
            employee.setUser( user );
            Employee savedEmployee = employeeRepository.save( employee );

            savedUser.setEmployee( savedEmployee );
        }

        return userAdminMapper.toUserAdminDTO(
                savedUser
        );
    }

    @Override
    @Transactional
    public UserAdminDTO updateUser( String id, UserUpdateDTO userUpdateDTO ) {
        User user = userRepository.findById( UUID.fromString( id ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find user with id: " + id )
                );

        return userAdminMapper.toUserAdminDTO(
                userRepository.save(
                        userAdminMapper.toUser( userUpdateDTO, user )
                )
        );
    }

    @Override
    @Transactional
    public void setUserPassword( String id, String newPassword, Boolean invalidateAllSessions ) {
        User user = userRepository.findById( UUID.fromString( id ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find user with id: " + id )
                );

        if ( invalidateAllSessions ) {
            sessionService.invalidateUserSessions( user.getUsername() );
        }

        user.setPassword( passwordEncoder.encode( newPassword ) );

        userRepository.save( user );
    }

    @Override
    @Transactional
    public void deleteUser( String id ) {
        User user = userRepository.findById( UUID.fromString( id ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find user with id: " + id )
                );

        if ( user.getEmployee() != null ) {
            Employee employee = user.getEmployee();
            employee.setUser( null );
            employeeRepository.save( employee );
        }

        userRepository.delete( user );
    }
}
