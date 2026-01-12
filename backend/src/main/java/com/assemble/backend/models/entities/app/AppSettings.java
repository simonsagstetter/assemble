/*
 * assemble
 * AppSettings.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.entities.app;

import com.assemble.backend.models.entities.core.BaseMongoEntity;
import com.assemble.backend.models.entities.employee.Address;
import com.assemble.backend.models.entities.holiday.SubdivisionCode;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "app_settings")
public class AppSettings extends BaseMongoEntity {

    @Builder.Default
    @NonNull
    private final String singletonId = "app_settings_singleton";

    private String companyName;

    private Address companyAddress;

    private SubdivisionCode holidaySubdivisionCode;

}
