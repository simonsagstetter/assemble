package com.assemble.backend.models.entities.holiday.api;

import lombok.Builder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

@Builder
public record HolidayResponse(
        @Nullable List<LocalizedText> comment,
        @NonNull String endDate,
        @NonNull String id,
        @NonNull List<LocalizedText> name,
        @NonNull Boolean nationwide,
        @NonNull String startDate,
        @Nullable List<SubdivisionReference> subdivisions,
        TemporalScope temporalScope
) {
}
