/*
 * assemble
 * TimeValuesValidator.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.dtos.timeentry.validators;

import com.assemble.backend.models.dtos.timeentry.TimeEntryCreateDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;
import java.time.Instant;

public class TimeValuesValidator implements ConstraintValidator<ValidTimeValues, TimeEntryCreateDTO> {
    @Override
    public boolean isValid( TimeEntryCreateDTO timeEntryCreateDTO, ConstraintValidatorContext constraintValidatorContext ) {
        if ( timeEntryCreateDTO == null ) return true;

        constraintValidatorContext.disableDefaultConstraintViolation();

        Instant start = timeEntryCreateDTO.getStartTime();
        Instant end = timeEntryCreateDTO.getEndTime();
        Duration total = timeEntryCreateDTO.getTotalTime();
        Duration pause = timeEntryCreateDTO.getPauseTime();

        boolean hasRange = start != null && end != null;
        boolean hasTotal = total != null;
        boolean hasPause = pause != null;

        if ( hasRange == hasTotal ) {
            String tmpl = "Start time and end time or total time must be set";
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate( tmpl )
                    .addPropertyNode( "startTime" )
                    .addConstraintViolation();

            constraintValidatorContext
                    .buildConstraintViolationWithTemplate( tmpl )
                    .addPropertyNode( "endTime" )
                    .addConstraintViolation();

            constraintValidatorContext
                    .buildConstraintViolationWithTemplate( tmpl )
                    .addPropertyNode( "totalTime" )
                    .addConstraintViolation();

            return false;
        }

        if ( hasRange && !start.isBefore( end ) ) {
            String tmpl = "Start time must be before end time";
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate( tmpl )
                    .addPropertyNode( "startTime" )
                    .addConstraintViolation();

            constraintValidatorContext
                    .buildConstraintViolationWithTemplate( tmpl )
                    .addPropertyNode( "endTime" )
                    .addConstraintViolation();

            return false;
        }

        if ( hasTotal && hasPause && total.minus( pause ).isNegative() ) {
            String tmpl = "Total time must be greater than pause time";

            constraintValidatorContext
                    .buildConstraintViolationWithTemplate( tmpl )
                    .addPropertyNode( "totalTime" )
                    .addConstraintViolation();

            constraintValidatorContext
                    .buildConstraintViolationWithTemplate( tmpl )
                    .addPropertyNode( "pauseTime" )
                    .addConstraintViolation();

            return false;
        }

        return true;
    }

    @Override
    public void initialize( ValidTimeValues constraintAnnotation ) {
        ConstraintValidator.super.initialize( constraintAnnotation );
    }
}
