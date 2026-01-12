/*
 * assemble
 * SubdivisionCode.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.models.entities.holiday;

import lombok.Getter;

@Getter
public enum SubdivisionCode {
    BB( "Brandenburg" ),
    BE( "Berlin" ),
    BW( "Baden-Württemberg" ),
    BY( "Bayern" ),
    HB( "Bremen" ),
    HE( "Hessen" ),
    HH( "Hamburg" ),
    MV( "Mecklenburg-Vorpommern" ),
    NI( "Niedersachsen" ),
    NW( "Nordrhein-Westfalen" ),
    RP( "Rheinland-Pfalz" ),
    SH( "Schleswig-Holstein" ),
    SL( "Saarland" ),
    SN( "Sachsen" ),
    ST( "Sachsen-Anhalt" ),
    TH( "Thüringen" );

    private final String label;

    SubdivisionCode( String label ) {
        this.label = label;
    }
}
