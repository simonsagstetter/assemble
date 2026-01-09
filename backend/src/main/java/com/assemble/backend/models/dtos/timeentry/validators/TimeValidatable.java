/*
 * assemble
 * TimeValidatable.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.dtos.timeentry.validators;

import java.time.Duration;
import java.time.Instant;

public interface TimeValidatable {
    Instant getStartTime();

    Instant getEndTime();

    Duration getTotalTime();

    Duration getPauseTime();
}
