/*
 * assemble
 * HolidayImportRestControllerTest.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.controllers.rest.holiday;

import com.assemble.backend.models.entities.auth.UserRole;
import com.assemble.backend.models.entities.holiday.Holiday;
import com.assemble.backend.models.entities.holiday.Subdivision;
import com.assemble.backend.models.entities.holiday.api.TemporalScope;
import com.assemble.backend.repositories.holiday.HolidayRepository;
import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import com.assemble.backend.testutils.WithMockCustomUser;
import com.github.f4b6a3.uuid.UuidCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("HolidayImportRestController Integeration Test")
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
class HolidayImportRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    private HolidayRepository holidayRepository;

    private static Holiday holiday;

    @BeforeEach
    void init() {
        List<Subdivision> subdivisions = List.of(
                Subdivision.builder()
                        .code( "DE-BE" )
                        .isoCode( "DE-BE" )
                        .name( "Berlin" )
                        .shortName( "DE-BE" )
                        .build()
        );
        holiday = Holiday.builder()
                .externalId( UuidCreator.getTimeOrderedEpoch().toString() )
                .name( "Test Holiday" )
                .startDate( LocalDate.now() )
                .endDate( LocalDate.now() )
                .nationWide( false )
                .subdivisions( subdivisions )
                .temporalScope( TemporalScope.FullDay )
                .build();
    }

    private void mockOpenHolidayApi() throws IOException {
        String holidayJsonContent = StreamUtils.copyToString(
                new ClassPathResource( "data/openholidayapi_holidays.json" ).getInputStream(),
                StandardCharsets.UTF_8
        );
        String subdivisionJsonContent = StreamUtils.copyToString(
                new ClassPathResource( "data/openholidayapi_subdivisions.json" ).getInputStream(),
                StandardCharsets.UTF_8
        );
        mockRestServiceServer.expect( requestTo(
                        "https://openholidaysapi.org/PublicHolidays?countryIsoCode=DE&languageIsoCode=DE&validFrom=2026-01-01&validTo=2026-12-31"
                ) )
                .andExpect(
                        method( HttpMethod.GET )
                ).andRespond(
                        withSuccess()
                                .contentType( MediaType.APPLICATION_JSON )
                                .body( holidayJsonContent )
                );

        mockRestServiceServer.expect( requestTo(
                        "https://openholidaysapi.org/Subdivisions?countryIsoCode=DE&languageIsoCode=DE"
                ) )
                .andExpect(
                        method( HttpMethod.GET )
                ).andRespond(
                        withSuccess()
                                .contentType( MediaType.APPLICATION_JSON )
                                .body( subdivisionJsonContent )
                );
    }

    private void performImportMockAction() throws Exception {
        mockMvc.perform(
                post( "/api/holidays/import/2026" )
                        .with( csrf() )
        ).andExpect(
                status().isOk()
        ).andExpect(
                MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                MockMvcResultMatchers.jsonPath( "$[0].id" ).isNotEmpty()
        ).andExpect(
                MockMvcResultMatchers.jsonPath( "$[0].startDate" ).isNotEmpty()
        ).andExpect(
                MockMvcResultMatchers.jsonPath( "$[0].endDate" ).isNotEmpty()
        ).andExpect(
                MockMvcResultMatchers.jsonPath( "$[0].name" ).isNotEmpty()
        ).andExpect(
                MockMvcResultMatchers.jsonPath( "$[0].externalId" ).isNotEmpty()
        );
    }

    @Test
    @DisplayName("/POST getHolidaysFromYear should return statuss code 200 and a empty list when holidays already exist")
    @WithMockCustomUser(roles = { UserRole.SUPERUSER })
    void getHolidaysFromYear_ShouldReturnStatusCode200AndAEmptyList_WhenHolidaysAlreadyExist() throws Exception {
        mockOpenHolidayApi();
        performImportMockAction();

        long count = holidayRepository.count();
        assertEquals( 10, count );

        mockRestServiceServer.reset();
        mockOpenHolidayApi();

        mockMvc.perform(
                post( "/api/holidays/import/2026" )
                        .with( csrf() )
        ).andExpect(
                status().isOk()
        ).andExpect(
                MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                MockMvcResultMatchers.content().string( "[]" )
        );
    }

    @Test
    @DisplayName("/POST getHolidaysFromYear should return status code 200 and a list of holiday dto")
    @WithMockCustomUser(roles = { UserRole.SUPERUSER })
    void getHolidaysFromYear_ShouldReturnStatusCode200AndAListOfHolidayDTO() throws Exception {
        mockOpenHolidayApi();
        performImportMockAction();

        long count = holidayRepository.count();
        assertEquals( 10, count );
    }

    @Test
    @DisplayName("/GET getImportedYears should return status code 200 and a list of integers")
    @WithMockCustomUser(roles = { UserRole.SUPERUSER })
    void getImportedYears_ShouldReturnStatusCode200AndAListOfIntegers_WhenCalled() throws Exception {
        Holiday savedHoliday = holidayRepository.save( holiday );

        mockMvc.perform(
                get( "/api/holidays/import/years" )
        ).andExpect(
                status().isOk()
        ).andExpect(
                MockMvcResultMatchers.content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                MockMvcResultMatchers.jsonPath( "$[0]" ).value( savedHoliday.getStartDate().getYear() )
        );
    }

    @Test
    @DisplayName("/DELETE deleteHolidaysFromYear should return status code 204")
    @WithMockCustomUser(roles = { UserRole.SUPERUSER })
    void deleteHolidaysFromYear_ShouldReturnStatusCode204_WhenCalled() throws Exception {
        holidayRepository.save( holiday );

        mockMvc.perform(
                delete( "/api/holidays/import/{year}", holiday.getStartDate().getYear() )
                        .with( csrf() )
        ).andExpect(
                status().isNoContent()
        );

        long count = holidayRepository.count();
        assertEquals( 0, count );
    }

}