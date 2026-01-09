/*
 * assemble
 * datetime.ts
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { formatISO, getDay, parse } from "date-fns";

function getDayMondayBased( date: Date ) {
    const day = getDay( date );
    return day === 0 ? 6 : day - 1;
}

function toInstant( date: Date, time?: string ) {
    if ( !time ) return undefined;
    return formatISO( parse( time, "HH:mm", date ) );
}

export {
    getDayMondayBased,
    toInstant
}