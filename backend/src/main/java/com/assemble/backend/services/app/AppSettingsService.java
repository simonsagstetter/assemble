/*
 * assemble
 * AppSettingsService.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.app;

import com.assemble.backend.models.dtos.app.AppSettingsDTO;

public interface AppSettingsService {
    AppSettingsDTO getSettings();

    AppSettingsDTO updateSettings( AppSettingsDTO appSettingsDTO );
}
