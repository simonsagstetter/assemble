/*
 * assemble
 * HolidayImportService.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.holiday;

import com.assemble.backend.models.entities.holiday.Holiday;
import com.assemble.backend.models.entities.holiday.api.HolidayResponse;
import com.assemble.backend.models.entities.holiday.api.SubdivisionResponse;

import java.util.List;

public interface HolidayImportService {

    List<Holiday> importHolidaysByYear( int year );

    List<HolidayResponse> getHolidaysByYear( int year );

    List<SubdivisionResponse> importSubdivisions();

    List<Integer> getImportedYears();

    void deleteHolidaysByYear( int year );
}
