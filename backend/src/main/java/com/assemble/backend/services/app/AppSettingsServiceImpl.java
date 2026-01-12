/*
 * assemble
 * AppSettingsServiceImpl.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.app;

import com.assemble.backend.models.dtos.app.AppSettingsDTO;
import com.assemble.backend.models.entities.app.AppSettings;
import com.assemble.backend.models.mappers.app.AppSettingsMapper;
import com.assemble.backend.repositories.app.AppSettingsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AppSettingsServiceImpl implements AppSettingsService {

    private AppSettingsRepository appSettingsRepository;
    private AppSettingsMapper appSettingsMapper;

    @Transactional(readOnly = true)
    @Override
    public AppSettingsDTO getSettings() {
        return appSettingsMapper.toAppSettingsDTO( appSettingsRepository.getSingleton() );
    }

    @Override
    @Transactional
    public AppSettingsDTO updateSettings( AppSettingsDTO appSettingsDTO ) {
        AppSettings appSettings = appSettingsRepository.getSingleton();
        appSettings.setCompanyName( appSettingsDTO.getCompanyName() );
        appSettings.setCompanyAddress( appSettingsDTO.getCompanyAddress() );
        appSettings.setHolidaySubdivisionCode( appSettingsDTO.getHolidaySubdivisionCode() );
        return appSettingsMapper.toAppSettingsDTO( appSettingsRepository.save( appSettings ) );
    }
}
