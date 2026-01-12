package com.assemble.backend.models.entities.holiday.api;

import org.springframework.lang.NonNull;

public record SubdivisionReference( @NonNull String code, @NonNull String shortName ) {
}
