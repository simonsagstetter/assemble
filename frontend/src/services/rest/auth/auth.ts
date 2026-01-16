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
import { me } from "@/api/rest/generated/fetch/users/users";
import { redirect } from "next/navigation";
import { LOGOUT_PATH } from "@/config/auth/auth.config";
import { getCookieHeader, getCsrfTokenHeader, setCookiesFromResponse, } from "@/utils/header";

const submitLogin = async ( data: LoginRequest ) => {
    const response = await login( data );
    if ( response.status === 200 ) {
        await setCookiesFromResponse( response.headers.getSetCookie() );
    }
    return response
}

const submitLogout = async () => {
    const cookie = await getCookieHeader();
    const csrf = await getCsrfTokenHeader();
    const response = await logout( {
        headers: {
            ...cookie,
            ...csrf
        }
    } );

    if ( response.status === 204 ) {
        await setCookiesFromResponse( response.headers.getSetCookie() );
    }

    return response;
}

const getUserDetails = async () => {
    const cookie = await getCookieHeader();
    const csrf = await getCsrfTokenHeader();
    const response = await me( {
        headers: {
            ...cookie,
            ...csrf
        }
    } )


    if ( response.status === 401 || response.status === 404 ) redirect( LOGOUT_PATH );

    return response.data;
}


export { submitLogin, submitLogout, getUserDetails }