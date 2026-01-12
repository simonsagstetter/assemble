/*
 * assemble
 * AppSettingsBootstrap.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.bootstrap.app;

import com.assemble.backend.models.entities.app.AppSettings;
import com.assemble.backend.models.entities.employee.Address;
import com.assemble.backend.models.entities.holiday.SubdivisionCode;
import com.assemble.backend.repositories.app.AppSettingsRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AppSettingsBootstrap implements CommandLineRunner {

    private AppSettingsRepository appSettingsRepository;

    @Override
    public void run( String... args ) throws Exception {
        long count = appSettingsRepository.count();
        if ( count == 0 ) {

            Address address = Address.builder()
                    .street( "" )
                    .number( "" )
                    .postalCode( "" )
                    .city( "" )
                    .state( "" )
                    .country( "" )
                    .build();

            AppSettings appSettings = AppSettings.builder()
                    .companyName( "" )
                    .companyAddress( address )
                    .holidaySubdivisionCode( SubdivisionCode.BE )
                    .build();

            appSettingsRepository.save( appSettings );
        }
    }
}
