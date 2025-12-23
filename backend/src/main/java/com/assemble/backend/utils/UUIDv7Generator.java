/*
 * assemble
 * UUIDv7Generator.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.utils;

import com.github.f4b6a3.uuid.UuidCreator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class UUIDv7Generator implements IdentifierGenerator {

    @Override
    public Object generate( SharedSessionContractImplementor sharedSessionContractImplementor, Object o ) {
        return UuidCreator.getTimeOrderedEpoch();
    }
}
