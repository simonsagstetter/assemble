package com.assemble.backend.models.entities.holiday.api;

import java.util.List;

public record SubdivisionResponse( String code, String isoCode, String shortName, List<LocalizedText> name,
                                   List<SubdivisionResponse> children ) {
}
