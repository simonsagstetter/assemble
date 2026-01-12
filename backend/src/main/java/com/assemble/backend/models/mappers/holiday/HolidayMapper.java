/*
 * assemble
 * HolidayMapper.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.mappers.holiday;

import com.assemble.backend.models.dtos.holiday.HolidayDTO;
import com.assemble.backend.models.entities.holiday.Holiday;
import com.assemble.backend.models.entities.holiday.Subdivision;
import com.assemble.backend.models.entities.holiday.api.HolidayResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface HolidayMapper {

    @BeanMapping(ignoreByDefault = true, nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
    @Mapping(target = "externalId", source = "holidayResponse.id")
    @Mapping(target = "name", expression = "java(holidayResponse.name().getFirst().text())")
    @Mapping(target = "startDate", source = "holidayResponse.startDate")
    @Mapping(target = "endDate", source = "holidayResponse.endDate")
    @Mapping(target = "nationWide", source = "holidayResponse.nationwide")
    @Mapping(target = "temporalScope", source = "holidayResponse.temporalScope")
    @Mapping(target = "subdivisions", source = "subdivisionList")
    Holiday toHoliday( HolidayResponse holidayResponse, List<Subdivision> subdivisionList );

    HolidayDTO toHolidayDTO( Holiday holiday );
}
