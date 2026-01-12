/*
 * assemble
 * AppSettingsMapper.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.mappers.app;

import com.assemble.backend.models.dtos.app.AppSettingsDTO;
import com.assemble.backend.models.entities.app.AppSettings;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface AppSettingsMapper {

    AppSettingsDTO toAppSettingsDTO( AppSettings appSettings );

}
