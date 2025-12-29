/*
 * assemble
 * EmployeeServiceImpl.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.employee;

import com.assemble.backend.models.dtos.employee.*;
import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.models.mappers.employee.EmployeeMapper;
import com.assemble.backend.repositories.auth.UserRepository;
import com.assemble.backend.repositories.employee.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map( employeeMapper::employeeToEmployeeDTO )
                .toList();
    }

    @Override
    public EmployeeDTO getEmployeeById( String id ) {
        Employee employee = employeeRepository.findById( UUID.fromString( id ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find employee with id: " + id )
                );
        return employeeMapper.employeeToEmployeeDTO( employee );
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeRefDTO> searchUnlinkedEmployees( String searchTerm ) {
        String normalizedSearchTerm = searchTerm == null ? "" : searchTerm.toLowerCase();
        return employeeRepository
                .search( normalizedSearchTerm )
                .stream()
                .map( employeeMapper::employeeToEmployeeRefDTO )
                .toList();
    }

    @Override
    @Transactional
    public EmployeeDTO createEmployee( EmployeeCreateDTO employeeCreateDTO ) {
        User user = employeeCreateDTO.getUserId() != null ? userRepository
                .findById( UUID.fromString( employeeCreateDTO.getUserId() ) )
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Could not find user with id: " + employeeCreateDTO.getUserId()
                        )
                )
                : null;

        if ( user != null && user.getEmployee() != null ) throw new InvalidParameterException(
                "User is already linked to employee " + user.getEmployee().getFullname()
        );

        Employee employee = employeeMapper.toEmployee( employeeCreateDTO );
        employee.setUser( user );


        return employeeMapper.employeeToEmployeeDTO(
                employeeRepository.save( employee )
        );
    }

    @Override
    @Transactional
    public EmployeeDTO setEmployeeUser( String employeeId, EmployeeUpdateUserDTO employeeUpdateUserDTO ) {
        Employee employee = employeeRepository.findById( UUID.fromString( employeeId ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find employee with id: " + employeeId )
                );

        User user = employeeUpdateUserDTO.getUserId() != null ? userRepository
                .findById( UUID.fromString( employeeUpdateUserDTO.getUserId() ) )
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Could not find user with id: " + employeeUpdateUserDTO.getUserId()
                        )
                )
                : null;

        if ( user == null ) {
            employee.setUser( null );
        } else {
            if ( user.getEmployee() != null ) throw new InvalidParameterException(
                    "User is already linked to employee " + user.getEmployee().getFullname()
            );
            employee.setUser( user );
        }
        employeeRepository.save( employee );

        return employeeMapper.employeeToEmployeeDTO( employee );
    }

    @Override
    @Transactional
    public EmployeeDTO updateEmployee( String id, EmployeeUpdateDTO employeeUpdateDTO ) {
        Employee employee = employeeRepository.findById( UUID.fromString( id ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find employee with id: " + id )
                );


        return employeeMapper.employeeToEmployeeDTO(
                employeeRepository.save( employeeMapper
                        .toEmployee( employeeUpdateDTO, employee )
                )
        );
    }

    @Override
    @Transactional
    public void deleteEmployee( String id ) {
        Employee employee = employeeRepository.findById( UUID.fromString( id ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find employee with id: " + id )
                );

        if ( employee.getUser() != null ) {
            User user = employee.getUser();
            user.setEmployee( null );
            userRepository.save( user );
        }

        employeeRepository.delete( employee );
    }
}
