package com.assemble.backend.models.entities.holiday.api;

import org.springframework.lang.NonNull;

public record LocalizedText( @NonNull String language, @NonNull String text ) {
}
