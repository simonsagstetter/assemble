/*
 * assemble
 * BankAccountMapper.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.mappers.employee;

import com.assemble.backend.models.dtos.employee.EmployeeCreateDTO;
import com.assemble.backend.models.entities.employee.BankAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface BankAccountMapper {
    @Mapping(target = "holderName", source = "bankAccount.holderName")
    @Mapping(target = "institutionName", source = "bankAccount.institutionName")
    @Mapping(target = "iban", source = "bankAccount.iban")
    @Mapping(target = "bic", source = "bankAccount.bic")
    @Named("bankAccountFromEmployeeCreateDTO")
    BankAccount fromEmployeeCreateDTO( EmployeeCreateDTO employeeCreateDTO );
}
