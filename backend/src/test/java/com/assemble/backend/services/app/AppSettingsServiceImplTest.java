/*
 * assemble
 * AppSettingsServiceImplTest.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.app;

import com.assemble.backend.models.dtos.app.AppSettingsDTO;
import com.assemble.backend.models.entities.app.AppSettings;
import com.assemble.backend.models.entities.employee.Address;
import com.assemble.backend.models.entities.holiday.SubdivisionCode;
import com.assemble.backend.models.mappers.app.AppSettingsMapper;
import com.assemble.backend.repositories.app.AppSettingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AppSettingsServiceImpl Unit Test")
class AppSettingsServiceImplTest {

    @Mock
    private AppSettingsRepository appSettingsRepository;

    @Mock
    private AppSettingsMapper appSettingsMapper;

    @InjectMocks
    private AppSettingsServiceImpl service;

    private static AppSettings appSettings;
    private static AppSettingsDTO appSettingsDTO;

    @BeforeEach
    void init() {
        Address address = Address.builder()
                .street( "" )
                .number( "" )
                .postalCode( "" )
                .city( "" )
                .state( "" )
                .country( "" )
                .build();

        appSettings = AppSettings.builder()
                .companyName( "Assemble" )
                .companyAddress( address )
                .holidaySubdivisionCode( SubdivisionCode.BE )
                .build();

        appSettingsDTO = AppSettingsDTO.builder()
                .companyName( appSettings.getCompanyName() )
                .companyAddress( appSettings.getCompanyAddress() )
                .holidaySubdivisionCode( appSettings.getHolidaySubdivisionCode() )
                .build();
    }

    @Test
    @DisplayName("getSettings should return default settings")
    void getSettings_ShouldReturnDefaultSettings_WhenCalled() {
        when( appSettingsRepository.getSingleton() ).thenReturn( appSettings );
        when( appSettingsMapper.toAppSettingsDTO( appSettings ) ).thenReturn( appSettingsDTO );

        AppSettingsDTO actual = service.getSettings();

        assertEquals( appSettingsDTO, actual );

        verify( appSettingsRepository, times( 1 ) ).getSingleton();
        verify( appSettingsMapper, times( 1 ) ).toAppSettingsDTO( appSettings );
    }

    @Test
    @DisplayName("updateSettings should return updated settings")
    void updateSettings_ShouldReturnUpdatedSettings_WhenCalled() {
        AppSettingsDTO dto = AppSettingsDTO.builder()
                .companyName( appSettings.getCompanyName() )
                .companyAddress( appSettings.getCompanyAddress() )
                .holidaySubdivisionCode( SubdivisionCode.BY )
                .build();

        when( appSettingsRepository.getSingleton() ).thenReturn( appSettings );

        appSettings.setHolidaySubdivisionCode( dto.getHolidaySubdivisionCode() );

        when( appSettingsRepository.save( appSettings ) ).thenReturn( appSettings );
        when( appSettingsMapper.toAppSettingsDTO( appSettings ) ).thenReturn( dto );

        AppSettingsDTO actual = service.updateSettings( dto );

        assertEquals( dto, actual );

        verify( appSettingsRepository, times( 1 ) ).getSingleton();
        verify( appSettingsRepository, times( 1 ) ).save( appSettings );
        verify( appSettingsMapper, times( 1 ) ).toAppSettingsDTO( appSettings );
    }

}