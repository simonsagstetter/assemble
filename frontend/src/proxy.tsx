/*
 * assemble
 * proxy.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
import { NextRequest, NextResponse } from "next/server";
import { cookies } from "next/headers";
import { ALLOWED_PATHS, LOGIN_PATH, LOGIN_REDIRECT_PATH, LOGOUT_PATH } from "@/config/auth/auth.config";
import { submitLogout } from "@/services/rest/auth/auth";

export default async function proxy( request: NextRequest ) {
    const pathName = request.nextUrl.pathname;
    const params = request.nextUrl.searchParams;
    const cookieStore = await cookies();
    const sessionCookie = cookieStore.get( "SESSION" );

    // AUTO LOGOUT
    if ( pathName === LOGOUT_PATH ) {
        await submitLogout();
        cookieStore.delete( "SESSION" );
        const nextUrl = params.get( "next" ) || LOGIN_REDIRECT_PATH
        return NextResponse.redirect( new URL( `/auth/login?next=${ nextUrl }&referrer=SESSION_INVALID`, request.url ) );
    }

    // NOT AUTHENTICATED
    if ( !sessionCookie && !ALLOWED_PATHS.includes( pathName ) ) {
        return NextResponse.redirect( new URL( `${ LOGIN_PATH }?next=${ pathName }`, request.url ) );
    }

    // AUTHENTICATED
    if ( sessionCookie && ALLOWED_PATHS.includes( pathName ) ) {
        return NextResponse.redirect( new URL( LOGIN_REDIRECT_PATH, request.url ) );
    }

    // DEFAULT REDIRECT FOR /
    if ( pathName === "/" ) {
        return NextResponse.redirect( new URL( LOGIN_REDIRECT_PATH, request.url ) );
    }
}


export const config = {
    matcher: [
        // Exclude API routes, static files, image optimizations, and .png files
        '/((?!api|_next/static|_next/image|.*\\.png$).*)',
    ],
}