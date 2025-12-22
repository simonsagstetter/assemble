/*
 * assemble
 * MaritalStatusMapper.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.mappers.employee;

import com.assemble.backend.models.entities.employee.MaritalStatus;
import lombok.Generated;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface MaritalStatusMapper {
    @Generated
    default MaritalStatus toEntity( String value ) {
        if ( value == null || value.isBlank() ) {
            return null;
        }
        return MaritalStatus.valueOf( value.toUpperCase() );
    }
    
    @Generated
    default String toDto( MaritalStatus status ) {
        return status == null ? null : status.name();
    }
}
