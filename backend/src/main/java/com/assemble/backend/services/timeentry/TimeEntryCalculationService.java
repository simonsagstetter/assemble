/*
 * assemble
 * TimeEntryCalculationService.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.timeentry;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.InvalidParameterException;
import java.time.Duration;

@Service
public class TimeEntryCalculationService {

    private final MathContext mathContext = new MathContext(
            MathContext.DECIMAL64.getPrecision(), RoundingMode.HALF_UP
    );

    BigDecimal calculateTotal( Duration total, Duration pause, BigDecimal rate ) {
        if ( total == null || rate == null ) throw new InvalidParameterException( "Total or rate cannot be null" );

        Duration duration = pause != null ? total.minus( pause ) : total;

        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();

        BigDecimal amount = rate.multiply( BigDecimal.valueOf( hours ), mathContext );

        return amount.add(
                rate
                        .divide( BigDecimal.valueOf( 60 ), mathContext )
                        .multiply( BigDecimal.valueOf( minutes ), mathContext )
        );
    }
}
