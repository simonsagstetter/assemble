/*
 * assemble
 * HolidayServiceImplTest.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.holiday;

import com.assemble.backend.models.dtos.holiday.HolidayDTO;
import com.assemble.backend.models.entities.auth.UserAudit;
import com.assemble.backend.models.entities.holiday.Holiday;
import com.assemble.backend.models.entities.holiday.Subdivision;
import com.assemble.backend.models.entities.holiday.api.TemporalScope;
import com.assemble.backend.models.mappers.holiday.HolidayMapper;
import com.assemble.backend.repositories.holiday.HolidayRepository;
import com.github.f4b6a3.uuid.UuidCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("HolidayServiceImpl Unit Test")
class HolidayServiceImplTest {

    @Mock
    private HolidayRepository holidayRepository;

    @Mock
    private HolidayMapper holidayMapper;

    @InjectMocks
    private HolidayServiceImpl service;

    private static Holiday holiday;
    private static Holiday holidayNationWide;
    private static HolidayDTO holidayDTO;
    private static HolidayDTO holidayNationWideDTO;

    @BeforeEach
    void init() {
        UUID recordId = UuidCreator.getTimeOrderedEpoch();

        UserAudit userAudit = new UserAudit( null, "SYSTEM" );
        Instant now = Instant.now();

        List<Subdivision> subdivisions = List.of(
                Subdivision.builder()
                        .code( "DE-BE" )
                        .isoCode( "DE-BE" )
                        .name( "Berlin" )
                        .shortName( "DE-BE" )
                        .build()
        );

        holiday = Holiday.builder()
                .id( recordId )
                .externalId( UuidCreator.getTimeOrderedEpoch().toString() )
                .name( "Test Holiday" )
                .startDate( LocalDate.now() )
                .endDate( LocalDate.now() )
                .nationWide( false )
                .subdivisions( subdivisions )
                .temporalScope( TemporalScope.FullDay )
                .createdBy( userAudit )
                .lastModifiedBy( userAudit )
                .createdDate( now )
                .lastModifiedDate( now )
                .build();

        holidayNationWide = Holiday.builder()
                .id( recordId )
                .externalId( holiday.getExternalId() )
                .name( holiday.getName() )
                .startDate( holiday.getStartDate() )
                .endDate( holiday.getEndDate() )
                .nationWide( true )
                .subdivisions( List.of() )
                .temporalScope( holiday.getTemporalScope() )
                .createdBy( userAudit )
                .lastModifiedBy( userAudit )
                .createdDate( now )
                .lastModifiedDate( now )
                .build();

        holidayDTO = HolidayDTO.builder()
                .id( recordId.toString() )
                .startDate( holiday.getStartDate() )
                .endDate( holiday.getEndDate() )
                .subdivisions( holiday.getSubdivisions() )
                .name( holiday.getName() )
                .nationWide( holiday.getNationWide() )
                .temporalScope( holiday.getTemporalScope() )
                .build();

        holidayNationWideDTO = HolidayDTO.builder()
                .id( recordId.toString() )
                .startDate( holidayNationWide.getStartDate() )
                .endDate( holidayNationWide.getEndDate() )
                .subdivisions( holidayNationWide.getSubdivisions() )
                .name( holidayNationWide.getName() )
                .nationWide( holidayNationWide.getNationWide() )
                .temporalScope( holidayNationWide.getTemporalScope() )
                .build();
    }

    @Test
    @DisplayName("getHolidaysByYearAndSubdivisionCode should return empty list when no holidays imported yet")
    void getHolidaysByYearAndSubdivisionCode_ShouldReturnEmptyList_WhenNoHolidaysImportedYet() {
        int year = holiday.getStartDate().getYear();
        LocalDate startDate = LocalDate.of( year, 1, 1 );
        LocalDate endDate = LocalDate.of( year, 12, 31 );
        when( holidayRepository.searchHolidayByStartDateBetween( startDate, endDate ) ).thenReturn( List.of() );

        List<HolidayDTO> actual = service.getHolidaysByYearAndSubdivisionCode( year, "DE-BE" );

        assertEquals( 0, actual.size() );

        verify( holidayRepository, times( 1 ) ).searchHolidayByStartDateBetween( startDate, endDate );
    }

    @Test
    @DisplayName("getHolidaysByYearAndSubdivisionCode should return list of holidays when holidays imported")
    void getHolidaysByYearAndSubdivisionCode_ShouldReturnListOfHolidays_WhenHolidaysImported() {
        int year = holiday.getStartDate().getYear();
        LocalDate startDate = LocalDate.of( year, 1, 1 );
        LocalDate endDate = LocalDate.of( year, 12, 31 );
        when( holidayRepository.searchHolidayByStartDateBetween( startDate, endDate ) )
                .thenReturn( List.of( holiday, holidayNationWide ) );

        when( holidayMapper.toHolidayDTO( holiday ) ).thenReturn( holidayDTO );
        when( holidayMapper.toHolidayDTO( holidayNationWide ) ).thenReturn( holidayNationWideDTO );

        List<HolidayDTO> actual = service.getHolidaysByYearAndSubdivisionCode( year, "DE-BE" );

        assertEquals( 2, actual.size() );
        assertEquals( holidayDTO, actual.getFirst() );
        assertEquals( holidayNationWideDTO, actual.getLast() );

        verify( holidayRepository, times( 1 ) ).searchHolidayByStartDateBetween( startDate, endDate );
        verify( holidayMapper, times( 1 ) ).toHolidayDTO( holiday );
        verify( holidayMapper, times( 1 ) ).toHolidayDTO( holidayNationWide );
    }
}