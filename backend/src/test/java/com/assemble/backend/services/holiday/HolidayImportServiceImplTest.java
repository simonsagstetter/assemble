/*
 * assemble
 * HolidayImportServiceImplTest.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.holiday;

import com.assemble.backend.models.entities.auth.UserAudit;
import com.assemble.backend.models.entities.holiday.Holiday;
import com.assemble.backend.models.entities.holiday.Subdivision;
import com.assemble.backend.models.entities.holiday.api.TemporalScope;
import com.assemble.backend.models.mappers.holiday.HolidayMapper;
import com.assemble.backend.models.mappers.holiday.SubdivisionMapper;
import com.assemble.backend.repositories.holiday.HolidayRepository;
import com.github.f4b6a3.uuid.UuidCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("HolidayImportServiceImplTest Unit Test")
class HolidayImportServiceImplTest {

    @Mock
    private HolidayRepository holidayRepository;

    @Mock
    private HolidayMapper holidayMapper;

    @Mock
    private SubdivisionMapper subdivisionMapper;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.Builder restClientBuilder;

    private HolidayImportServiceImpl service;

    private static Holiday holiday;

    @BeforeEach
    void init() {

        when( restClientBuilder.baseUrl( anyString() ) ).thenReturn( restClientBuilder );
        when( restClientBuilder.build() ).thenReturn( restClient );

        service = new HolidayImportServiceImpl(
                restClientBuilder,
                holidayRepository,
                holidayMapper,
                subdivisionMapper
        );

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
    }


    @Test
    @DisplayName("getImportedYears should return a list of integers")
    void getImportedYears_ShouldReturnAListOfIntegers() {
        when( holidayRepository.findDistinctYears() ).thenReturn( List.of(
                holiday.getStartDate().getYear()
        ) );

        List<Integer> actual = service.getImportedYears();

        assertEquals( 1, actual.size() );
        assertEquals( holiday.getStartDate().getYear(), actual.getFirst() );
        verify( holidayRepository, times( 1 ) ).findDistinctYears();
    }

    @Test
    @DisplayName("deleteHolidaysByYear should delete all found holidays")
    void deleteHolidaysByYear_ShouldDeleteAllFoundHolidays_WhenCalled() {
        // Arrange
        int year = 2024;
        LocalDate startDate = LocalDate.of( year, 1, 1 );
        LocalDate endDate = LocalDate.of( year, 12, 31 );
        List<Holiday> holidays = List.of( holiday );

        when( holidayRepository.searchHolidayByStartDateBetween( startDate, endDate ) )
                .thenReturn( holidays );


        // Act
        assertDoesNotThrow( () -> service.deleteHolidaysByYear( year ) );

        // Assert
        verify( holidayRepository ).searchHolidayByStartDateBetween( startDate, endDate );
        verify( holidayRepository ).deleteAll( holidays );
    }

}