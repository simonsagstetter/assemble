/*
 * assemble
 * HolidayRestControllerTest.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.controllers.rest.holiday;

import com.assemble.backend.models.entities.holiday.Holiday;
import com.assemble.backend.models.entities.holiday.SubdivisionCode;
import com.assemble.backend.models.entities.holiday.api.TemporalScope;
import com.assemble.backend.repositories.holiday.HolidayRepository;
import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import com.assemble.backend.testutils.WithMockCustomUser;
import com.github.f4b6a3.uuid.UuidCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("HolidayRestController Integration Test")
@AutoConfigureMockMvc
class HolidayRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HolidayRepository holidayRepository;

    private static Holiday holiday;

    @BeforeEach
    void init() {
        holiday = Holiday.builder()
                .externalId( UuidCreator.getTimeOrderedEpoch().toString() )
                .name( "New Year" )
                .startDate( LocalDate.of( 2026, 1, 1 ) )
                .endDate( LocalDate.of( 2026, 1, 1 ) )
                .nationWide( true )
                .subdivisions( null )
                .temporalScope( TemporalScope.FullDay )
                .build();
    }

    @Test
    @DisplayName("/GET getHolidaysByYearAndSubdivisionCode should return status code 200 and empty list")
    @WithMockCustomUser
    void getHolidaysByYearAndSubdivisionCode_ShouldReturnStatusCode200AndEmptyList_WhenCalled() throws Exception {
        holidayRepository.save( holiday );
        mockMvc.perform(
                get( "/api/holidays?year=2025&subdivisionCode=DE-BE" )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( "application/json" )
        ).andExpect(
                jsonPath( "$" ).isEmpty()
        );
    }

    @Test
    @DisplayName("/GET getHolidaysByYearAndSubdivisionCode should return status code 200 and a list of holiday dto")
    @WithMockCustomUser
    void getHolidaysByYearAndSubdivisionCode_ShouldReturnStatusCode200AndAListOfHolidayDTO_WhenCalled() throws Exception {
        holidayRepository.save( holiday );
        mockMvc.perform(
                get( "/api/holidays?year=2026&subdivisionCode=DE-BE" )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( "application/json" )
        ).andExpect(
                jsonPath( "$" ).isArray()
        );
    }

    @Test
    @DisplayName("/GET getCompanySubdivisionCode should return status code 200 and subdivisioncode")
    @WithMockCustomUser
    void getCompanySubdivisionCode_ShouldReturnStatusCode200AndSubdivisionCode_WhenCalled() throws Exception {
        mockMvc.perform( get( "/api/holidays/settings" ) )
                .andExpect( status().isOk() )
                .andExpect( content().contentType( "application/json" ) )
                .andExpect( jsonPath( "$" ).value( SubdivisionCode.BE.toString() ) );
    }
}