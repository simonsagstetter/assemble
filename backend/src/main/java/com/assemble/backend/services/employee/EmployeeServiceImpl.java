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

import com.assemble.backend.models.dtos.employee.EmployeeCreateDTO;
import com.assemble.backend.models.dtos.employee.EmployeeDTO;
import com.assemble.backend.models.dtos.employee.EmployeeUpdateDTO;
import com.assemble.backend.models.dtos.employee.EmployeeUpdateUserDTO;
import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.models.mappers.employee.EmployeeMapper;
import com.assemble.backend.repositories.auth.UserRepository;
import com.assemble.backend.repositories.employee.EmployeeRepository;
import com.assemble.backend.services.core.IdService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final EmployeeMapper employeeMapper;
    private final IdService idService;

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map( employeeMapper::employeeToEmployeeDTO )
                .toList();
    }

    public EmployeeDTO getEmployeeById( String id ) {
        Employee employee = employeeRepository.findById( id )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find employee with id: " + id )
                );
        return employeeMapper.employeeToEmployeeDTO( employee );
    }

    @Override
    public EmployeeDTO createEmployee( EmployeeCreateDTO employeeCreateDTO ) {
        User user = employeeCreateDTO.getUserId() != null ? userRepository
                .findById( employeeCreateDTO.getUserId() )
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Could not find user with id: " + employeeCreateDTO.getUserId()
                        )
                )
                : null;

        if ( user != null && user.getEmployee() != null ) throw new InvalidParameterException(
                "User is already linked to employee " + user.getEmployee().getFullname()
        );

        String id = idService.generateIdFor( Employee.class );

        Employee employee = employeeMapper.toEmployee( employeeCreateDTO, id );
        employee.setUser( user );

        return employeeMapper.employeeToEmployeeDTO(
                employeeRepository.save( employee )
        );
    }

    @Override
    public EmployeeDTO setEmployeeUser( String employeeId, EmployeeUpdateUserDTO employeeUpdateUserDTO ) {
        Employee employee = employeeRepository.findById( employeeId )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find employee with id: " + employeeId )
                );

        User user = employeeUpdateUserDTO.getUserId() != null ? userRepository
                .findById( employeeUpdateUserDTO.getUserId() )
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
    public EmployeeDTO updateEmployee( String id, EmployeeUpdateDTO employeeUpdateDTO ) {
        Employee employee = employeeRepository.findById( id )
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
    public void deleteEmployee( String id ) {
        Employee employee = employeeRepository.findById( id )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find employee with id: " + id )
                );
        employeeRepository.delete( employee );
    }
}
