/*
 * assemble
 * header.ts
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
import { cookies } from "next/headers";
import { parseSetCookie, stringifyCookie } from "next/dist/compiled/@edge-runtime/cookies";

const getCookieHeader = async (): Promise<HeadersInit> => {
    return {
        cookie: ( await cookies() ).getAll().map( stringifyCookie ).join( "; " )
    }
}

const setCookiesFromResponse = async ( setCookie: string[] ) => {
    console.log( "Setting cookies:", setCookie );
    const cookieStore = await cookies();
    setCookie.forEach( cookie => {
        const parsedCookie = parseSetCookie( cookie );
        if ( parsedCookie ) cookieStore.set( parsedCookie );
    } );
}

const getCsrfTokenHeader = async (): Promise<HeadersInit> => {
    const cookieStore = await cookies();

    if ( cookieStore.has( "XSRF-TOKEN" ) ) {
        return {
            "X-XSRF-TOKEN": cookieStore.get( "XSRF-TOKEN" )!.value
        }
    }
    return {};
}

export { getCookieHeader, setCookiesFromResponse, getCsrfTokenHeader }