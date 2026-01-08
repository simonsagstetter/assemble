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
import com.assemble.backend.models.dtos.timeentry.TimeEntryUpdateDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;
import java.time.Instant;

public class TimeValuesValidator implements ConstraintValidator<ValidTimeValues, TimeValidatable> {

    @Override
    public boolean isValid( TimeValidatable dto, ConstraintValidatorContext constraintValidatorContext ) {
        if ( dto == null ) return true;

        constraintValidatorContext.disableDefaultConstraintViolation();

        Instant start = dto.getStartTime();
        Instant end = dto.getEndTime();
        Duration total = dto.getTotalTime();
        Duration pause = dto.getPauseTime();

        boolean hasRange = start != null && end != null;

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

        if ( !hasRange && total.minus( pause ).isNegative() ) {
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
