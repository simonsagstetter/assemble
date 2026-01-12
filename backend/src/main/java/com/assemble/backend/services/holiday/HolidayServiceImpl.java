/*
 * assemble
 * HolidayServiceImpl.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.holiday;

import com.assemble.backend.models.dtos.holiday.HolidayDTO;
import com.assemble.backend.models.entities.holiday.Holiday;
import com.assemble.backend.models.entities.holiday.Subdivision;
import com.assemble.backend.models.mappers.holiday.HolidayMapper;
import com.assemble.backend.repositories.holiday.HolidayRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class HolidayServiceImpl implements HolidayService {

    private HolidayRepository holidayRepository;

    private HolidayMapper holidayMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HolidayDTO> getHolidaysByYearAndSubdivisionCode( int year, String subdivisionCode ) {
        LocalDate startDate = LocalDate.of( year, 1, 1 );
        LocalDate endDate = LocalDate.of( year, 12, 31 );

        List<Holiday> holidays = holidayRepository.searchHolidayByStartDateBetween( startDate, endDate );

        return holidays.stream()
                .filter(
                        holiday -> holiday.getNationWide() ||
                                holiday.getSubdivisions().stream()
                                        .map( Subdivision::getCode ).toList()
                                        .contains( subdivisionCode )
                )
                .map( holidayMapper::toHolidayDTO )
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HolidayDTO getHolidayByDate( String date ) {
        AtomicReference<LocalDate> holidayDate = new AtomicReference<>();

        try {
            LocalDate parsedDate = LocalDate.parse( date );
            holidayDate.set( parsedDate );
        } catch ( DateTimeParseException e ) {
            throw new InvalidParameterException( "Invalid date format" );
        }


        Holiday holiday = holidayRepository.findByStartDate( holidayDate.get() )
                .orElseThrow( () -> new EntityNotFoundException( "Could not find holiday" ) );

        return holidayMapper.toHolidayDTO( holiday );
    }
}
