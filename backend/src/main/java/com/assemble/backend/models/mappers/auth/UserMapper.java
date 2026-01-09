/*
 * assemble
 * UserMapper.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.mappers.auth;

import com.assemble.backend.models.dtos.auth.UserDTO;
import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.employee.Employee;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "fullname", expression = "java(user.getFullname())")
    @Mapping(target = "employeeId", source = "employee", qualifiedByName = "mapEmployeeId")
    UserDTO toUserDTO( User user );

    @Named("mapEmployeeId")
    default String mapEmployeeId( Employee employee ) {
        return employee != null && employee.getId() != null ? employee.getId().toString() : null;
    }
}
