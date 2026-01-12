/*
 * assemble
 * HolidayImportServiceImpl.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.holiday;

import com.assemble.backend.models.entities.holiday.Holiday;
import com.assemble.backend.models.entities.holiday.Subdivision;
import com.assemble.backend.models.entities.holiday.api.HolidayResponse;
import com.assemble.backend.models.entities.holiday.api.SubdivisionResponse;
import com.assemble.backend.models.mappers.holiday.HolidayMapper;
import com.assemble.backend.models.mappers.holiday.SubdivisionMapper;
import com.assemble.backend.repositories.holiday.HolidayRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class HolidayImportServiceImpl implements HolidayImportService {

    private final RestClient restClient;

    private final HolidayRepository holidayRepository;

    private final HolidayMapper holidayMapper;

    private final SubdivisionMapper subdivisionMapper;

    public HolidayImportServiceImpl(
            RestClient.Builder restClient,
            HolidayRepository holidayRepository,
            HolidayMapper holidayMapper,
            SubdivisionMapper subdivisionMapper
    ) {
        this.restClient = restClient
                .baseUrl( "https://openholidaysapi.org/" )
                .build();
        this.holidayRepository = holidayRepository;
        this.holidayMapper = holidayMapper;
        this.subdivisionMapper = subdivisionMapper;
    }

    @Override
    @Transactional
    public List<Holiday> importHolidaysByYear( int year ) {
        Set<String> existingHolidayIds = holidayRepository.findAll().stream()
                .map( Holiday::getExternalId )
                .collect( Collectors.toSet() );

        List<HolidayResponse> holidayResponses = getHolidaysByYear( year );

        List<SubdivisionResponse> subdivisionResponses = importSubdivisions();
        Map<String, Subdivision> subdivisionMap = subdivisionResponses.stream()
                .map( subdivisionMapper::toSubdivision )
                .collect(
                        Collectors.toMap(
                                Subdivision::getCode,
                                Function.identity()
                        )
                );

        List<Holiday> holidays = holidayResponses.stream()
                .filter( holidayResponse -> !existingHolidayIds.contains( holidayResponse.id() ) )
                .map( holidayResponse -> {
                    List<Subdivision> subdivisions = !holidayResponse.nationwide()
                            && holidayResponse.subdivisions() != null ?
                            holidayResponse.subdivisions().stream()
                                    .map( subdivisionReference ->
                                            subdivisionMap.get( subdivisionReference.code() )
                                    )
                                    .filter( Objects::nonNull )
                                    .toList()
                            : null;
                    return holidayMapper.toHoliday( holidayResponse, subdivisions );
                } )
                .toList();


        return holidayRepository.saveAll( holidays );
    }

    @Override
    public List<HolidayResponse> getHolidaysByYear( int year ) {
        String validFrom = year + "-01-01";
        String validTo = year + "-12-31";

        return this.restClient
                .get()
                .uri( "PublicHolidays?countryIsoCode=DE&languageIsoCode=DE"
                                + "&validFrom={validFrom}"
                                + "&validTo={validTo}",
                        validFrom,
                        validTo
                )
                .accept( MediaType.APPLICATION_JSON )
                .acceptCharset( StandardCharsets.UTF_8 )
                .retrieve()
                .body( new ParameterizedTypeReference<List<HolidayResponse>>() {
                } );
    }

    public List<SubdivisionResponse> importSubdivisions() {
        return this.restClient
                .get()
                .uri( "Subdivisions?countryIsoCode=DE&languageIsoCode=DE" )
                .accept( MediaType.APPLICATION_JSON )
                .acceptCharset( StandardCharsets.UTF_8 )
                .retrieve()
                .body( new ParameterizedTypeReference<List<SubdivisionResponse>>() {
                } );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Integer> getImportedYears() {
        return holidayRepository.findDistinctYears();
    }

    @Override
    @Transactional
    public void deleteHolidaysByYear( int year ) {
        LocalDate startDate = LocalDate.of( year, 1, 1 );
        LocalDate endDate = LocalDate.of( year, 12, 31 );

        List<Holiday> holidays = holidayRepository.searchHolidayByStartDateBetween( startDate, endDate );
        holidayRepository.deleteAll( holidays );
    }
}
