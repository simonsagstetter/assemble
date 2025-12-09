/*
 * assemble
 * auth.ts
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use server";
import { login, logout } from "@/api/rest/generated/fetch/authentication/authentication";
import { LoginRequest } from "@/api/rest/generated/fetch/openAPIDefinition.schemas";
import { cookies } from "next/headers";
import { parseSetCookie, stringifyCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { me } from "@/api/rest/generated/fetch/users/users";
import { redirect } from "next/navigation";
import { LOGOUT_PATH } from "@/config/auth/auth.config";

const submitLogin = async ( data: LoginRequest ) => {
    const response = await login( data );

    if ( response.status === 200 ) {
        const cookieStore = await cookies();
        response.headers.getSetCookie().forEach( cookie => {
            const parsedCookie = parseSetCookie( cookie );
            if ( parsedCookie ) cookieStore.set( parsedCookie );
        } );
    }

    return response;
}

const submitLogout = async () => {
    const cookieStore = await cookies();
    const response = await logout( {
        credentials: "include",
        headers: {
            cookie: cookieStore.getAll().map( stringifyCookie ).join( "; " ),
        }
    } );

    if ( response.status === 204 ) {
        cookieStore.delete( "SESSION" );
    }

    return response;
}

const getUserDetails = async () => {
    const cookieStore = await cookies();
    const response = await me( {
        credentials: "include",
        headers: {
            cookie: cookieStore.getAll().map( stringifyCookie ).join( "; " ),
        }
    } )

    if ( response.status === 401 ) redirect( LOGOUT_PATH );


    return response.data;
}


export { submitLogin, submitLogout, getUserDetails }