/*
 * assemble
 * TimeEntryCalculationServiceTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.timeentry;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TimeEntryCalculationService Unit Test")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class TimeEntryCalculationServiceTest {

    private final TimeEntryCalculationService timeEntryCalculationService = new TimeEntryCalculationService();

    @Test
    @DisplayName("calculateTotal should return correct rate")
    void calculateTotalShouldReturnCorrectRate_WhenCalled() {
        BigDecimal rate = BigDecimal.valueOf( 60 );
        Duration total = Duration.ofHours( 6 );
        Duration pause = Duration.ofMinutes( 30 );
        BigDecimal expecedRate = BigDecimal.valueOf( 330 );

        BigDecimal actual = timeEntryCalculationService.calculateTotal( total, pause, rate );

        assertEquals( expecedRate, actual );

    }

    @Test
    @DisplayName("calculateTotal should return correct rate when pause is null")
    void calculateTotalShouldReturnCorrectRate_WhenCalledPauseIsNull() {
        BigDecimal rate = BigDecimal.valueOf( 60 );
        Duration total = Duration.ofHours( 6 );
        BigDecimal expecedRate = BigDecimal.valueOf( 360 );

        BigDecimal actual = timeEntryCalculationService.calculateTotal( total, null, rate );

        assertEquals( expecedRate, actual );
    }

    @Test
    @DisplayName("calculateTotal should throw when total duration is null")
    void calculateTotalShouldThrow_WhenCalledTotalDurationIsNull() {
        BigDecimal rate = BigDecimal.valueOf( 60 );

        assertThrows( InvalidParameterException.class, () ->
                timeEntryCalculationService.calculateTotal( null, null, rate )
        );

    }

    @Test
    @DisplayName("calculateTotal should throw when rate is null")
    void calculateTotalShouldThrow_WhenCalledRateIsNull() {
        Duration total = Duration.ofHours( 6 );

        assertThrows( InvalidParameterException.class, () ->
                timeEntryCalculationService.calculateTotal( total, null, null )
        );

    }

    @Test
    @DisplayName("calculateTotal should throw when rate and total duration is null")
    void calculateTotalShouldThrow_WhenCalledRateAndTotalDurationIsNull() {
        assertThrows( InvalidParameterException.class, () ->
                timeEntryCalculationService.calculateTotal( null, null, null )
        );

    }

}