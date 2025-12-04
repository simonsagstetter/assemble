package com.assemble.backend.services.core;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Service responsible for generating unique identifiers for given entity classes.
 * <p>
 * The generated ID consists of:
 * <ul>
 *     <li>The first and last lowercase letters of the entity class name</li>
 *     <li>A randomly generated UUID without hyphens, in lowercase</li>
 * </ul>
 * This ensures IDs are unique, compact, and traceable to their entity type.
 */
@Component
public class IdService {

    /**
     * Generates a unique identifier string for the specified entity class.
     * <p>
     * Example output for an entity class named <code>User</code>:
     * <pre>
     *     ur3f9b0e42341e4cd8b8fa12cd0240c85d
     * </pre>
     *
     * @param entityClass the class object representing the entity type
     * @param <T>         the type of the entity
     * @return a unique ID string prefixed with the first and last character
     * of the entity's class name, followed by a lowercase UUID without hyphens
     */
    public <T> String generateIdFor( Class<T> entityClass ) {
        String entityName = entityClass.getSimpleName().toLowerCase();
        String prefix = entityName.substring( 0, 1 );
        String suffix = entityName.substring( entityName.length() - 1 );
        return prefix + suffix
                + UUID.randomUUID()
                .toString().
                replaceAll( "-", "" )
                .toLowerCase();
    }
}
