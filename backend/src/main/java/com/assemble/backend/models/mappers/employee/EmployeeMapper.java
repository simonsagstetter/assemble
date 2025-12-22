/*
 * assemble
 * EmployeeMapper.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.mappers.employee;

import com.assemble.backend.models.dtos.employee.EmployeeCreateDTO;
import com.assemble.backend.models.dtos.employee.EmployeeDTO;
import com.assemble.backend.models.dtos.employee.EmployeeRefDTO;
import com.assemble.backend.models.dtos.employee.EmployeeUpdateDTO;
import com.assemble.backend.models.entities.employee.Employee;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { AddressMapper.class, BankAccountMapper.class, MaritalStatusMapper.class }
)
public interface EmployeeMapper {

    @Mapping(target = "fullname", source = "fullname")
    EmployeeDTO employeeToEmployeeDTO( Employee employee );

    @Named("employeeToEmployeeRefDTO")
    EmployeeRefDTO employeeToEmployeeRefDTO( Employee employee );

    @Mapping(target = "id", expression = "java(id)")
    @Mapping(target = "firstname", expression = "java(employeeCreateDTO.getFirstname())")
    @Mapping(target = "lastname", expression = "java(employeeCreateDTO.getLastname())")
    @Mapping(target = "email", expression = "java(employeeCreateDTO.getEmail())")
    @Mapping(target = "address", source = "employeeCreateDTO", qualifiedByName = "addressFromEmployeeCreateDTO")
    @Mapping(target = "bankAccount", source = "employeeCreateDTO", qualifiedByName = "bankAccountFromEmployeeCreateDTO")
    Employee toEmployee( EmployeeCreateDTO employeeCreateDTO, String id );

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    Employee toEmployee( EmployeeUpdateDTO employeeUpdateDTO, @MappingTarget Employee employee );

}
