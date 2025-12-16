/*
 * assemble
 * UserAdminMapper.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.mappers.auth;

import com.assemble.backend.models.dtos.auth.admin.UserAdminDTO;
import com.assemble.backend.models.dtos.auth.admin.UserCreateDTO;
import com.assemble.backend.models.dtos.auth.admin.UserUpdateDTO;
import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.mappers.employee.EmployeeMapper;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = EmployeeMapper.class
)
public interface UserAdminMapper {

    @Mapping(target = "fullname", expression = "java(user.getFullname())")
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeToEmployeeRefDTO")
    UserAdminDTO toUserAdminDTO( User user );

    @Mapping(target = "id", expression = "java(id)")
    @Mapping(target = "firstname", expression = "java(userCreateDTO.getFirstname())")
    @Mapping(target = "lastname", expression = "java(userCreateDTO.getLastname())")
    @Mapping(target = "email", expression = "java(userCreateDTO.getEmail())")
    @Mapping(target = "enabled", expression = "java(true)")
    User toUser( UserCreateDTO userCreateDTO, String id );

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User toUser( UserUpdateDTO userUpdateDTO, @MappingTarget User user );
}
