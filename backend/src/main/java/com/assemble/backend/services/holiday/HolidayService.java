/*
 * assemble
 * HolidayService.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.holiday;

import com.assemble.backend.models.dtos.holiday.HolidayDTO;

import java.util.List;

public interface HolidayService {

    List<HolidayDTO> getHolidaysByYearAndSubdivisionCode( int year, String subdivisionCode );

    HolidayDTO getHolidayByDate( String date );

}
