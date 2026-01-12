/*
 * assemble
 * SubdivisionMapper.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.mappers.holiday;

import com.assemble.backend.models.entities.holiday.Subdivision;
import com.assemble.backend.models.entities.holiday.api.SubdivisionResponse;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface SubdivisionMapper {

    @BeanMapping(ignoreByDefault = true, nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    @Mapping(target = "code", source = "subdivisionResponse.code")
    @Mapping(target = "isoCode", source = "subdivisionResponse.isoCode")
    @Mapping(target = "shortName", source = "subdivisionResponse.shortName")
    @Mapping(target = "name", expression = "java(subdivisionResponse.name().getFirst().text())")
    Subdivision toSubdivision( SubdivisionResponse subdivisionResponse );
}
