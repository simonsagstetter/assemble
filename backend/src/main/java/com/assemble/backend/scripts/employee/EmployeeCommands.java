/*
 * assemble
 * EmployeeCommands.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.scripts.employee;

import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.employee.Address;
import com.assemble.backend.models.entities.employee.BankAccount;
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.models.entities.employee.MaritalStatus;
import com.assemble.backend.repositories.auth.UserRepository;
import com.assemble.backend.repositories.employee.EmployeeRepository;
import com.assemble.backend.services.core.IdService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@ShellComponent
@AllArgsConstructor
public class EmployeeCommands {

    private UserRepository userRepository;

    private EmployeeRepository employeeRepository;

    private IdService idService;

    @ShellMethod(
            key = "create-employee"
    )
    public String createEmployee(
            @ShellOption(defaultValue = "") String u
    ) {
        if ( u.isEmpty() ) {
            throw new InvalidParameterException( "u parameter cannot be empty" );
        }

        Optional<User> response = userRepository.findByUsername( u );

        if ( response.isEmpty() ) {
            throw new EntityNotFoundException( "User not found" );
        }

        User user = response.get();

        if ( user.getEmployee() != null ) throw new InvalidParameterException(
                "User is already linked to another employee"
        );

        Employee employee = Employee.builder()
                .id( idService.generateIdFor( Employee.class ) )
                .user( user )
                .firstname( user.getFirstname() )
                .lastname( user.getLastname() )
                .email( user.getEmail() )
                .address( Address.builder()
                        .street( "Weserstraße" )
                        .number( "190" )
                        .city( "Berlin" )
                        .postalCode( "12045" )
                        .country( "Germany" )
                        .state( "Berlin" )
                        .build() )
                .bankAccount( BankAccount.builder()
                        .holderName( "Simon Sagstetter" )
                        .institutionName( "Best Bank" )
                        .iban( "DE336464646927482834" )
                        .bic( "BX9384800" )
                        .build()
                )
                .maritalStatus( MaritalStatus.SINGLE )
                .placeOfBirth( "Berlin" )
                .dateOfBirth( LocalDate.of( 1995, 1, 1 ) )
                .build();

        Employee savedEmployee = employeeRepository.save( employee );

        user.setEmployee( savedEmployee );
        userRepository.save( user );

        log.info( "Created employee with id: {}", savedEmployee.getId() );

        return employee.getId();
    }
}
