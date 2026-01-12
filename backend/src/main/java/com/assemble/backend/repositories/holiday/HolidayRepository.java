/*
 * assemble
 * HolidayRepository.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.repositories.holiday;

import com.assemble.backend.models.entities.holiday.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HolidayRepository extends JpaRepository<Holiday, UUID> {

    List<Holiday> searchHolidayByStartDateBetween( LocalDate startDateAfter, LocalDate startDateBefore );

    Optional<Holiday> findByStartDate( LocalDate startDate );

    @Query("SELECT DISTINCT YEAR(h.startDate) FROM Holiday h ORDER BY YEAR(h.startDate)")
    List<Integer> findDistinctYears();
    
}
