/*
 * assemble
 * AppSettingsRepository.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.repositories.app;

import com.assemble.backend.models.entities.app.AppSettings;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppSettingsRepository extends MongoRepository<AppSettings, String> {
    default AppSettings getSingleton() {
        return findBySingletonId( "app_settings_singleton" );
    }

    AppSettings findBySingletonId( String singletonId );
}
