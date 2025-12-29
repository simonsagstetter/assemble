/*
 * assemble
 * NoGenerator.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.utils;

import com.assemble.backend.models.entities.db.Sequence;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class NoGenerator {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public String generateNextNo( String sequenceName, String prefix ) {
        String query = "SELECT s FROM Sequence s WHERE s.name = :sequenceName";

        try {
            Sequence sequence = entityManager
                    .createQuery( query, Sequence.class )
                    .setParameter( "sequenceName", sequenceName )
                    .setLockMode( LockModeType.PESSIMISTIC_WRITE )
                    .getSingleResult();

            sequence.setCurrentValue( sequence.getCurrentValue() + 1 );
            entityManager.merge( sequence );

            return String.format( "%s%05d", prefix, sequence.getCurrentValue() );
        } catch ( NoResultException e ) {
            Sequence sequence = Sequence.builder()
                    .name( sequenceName )
                    .currentValue( 1L )
                    .build();

            entityManager.persist( sequence );

            return String.format( "%s%05d", prefix, sequence.getCurrentValue() );
        }
    }
}
