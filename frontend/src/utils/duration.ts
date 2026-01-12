/*
 * assemble
 * duration.ts
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
import moment from "moment";

function parseIsoDuration( iso: string ) {
    const duration = moment.duration( iso );
    return { hours: duration.hours(), minutes: duration.minutes(), seconds: duration.seconds() };
}

function isoDurationToMs( iso: string ) {
    const { hours, minutes } = parseIsoDuration( iso );
    return ( hours * 60 + minutes ) * 60 * 1000;
}

function msToHHmm( ms: number ): string {
    const totalMinutes = Math.floor( ms / ( 60 * 1000 ) );
    const h = Math.floor( totalMinutes / 60 );
    const m = totalMinutes % 60;

    return `${ String( h ).padStart( 2, "0" ) }:${ String( m ).padStart( 2, "0" ) }`;
}

function HHmmToMs( time: string ) {
    const [ h, m ] = time.split( ":" ).map( Number );
    return h * 60 * 60 * 1000 + m * 60 * 1000;
}

function toDuration( time?: string ) {
    if ( !time ) return undefined;
    const [ h, m ] = time.split( ":" ).map( Number );

    return `PT${ h }H${ m }M`;
}

export {
    toDuration,
    isoDurationToMs,
    msToHHmm,
    HHmmToMs
}