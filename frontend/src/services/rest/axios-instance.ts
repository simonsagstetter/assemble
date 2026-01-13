/*
 * assemble
 * axios-server-instance.ts
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */


import Axios, { AxiosError, AxiosRequestConfig } from 'axios';

export const AXIOS_INSTANCE = Axios.create( { baseURL: process.env.BACKEND_API_CLIENT_URL } );

export const instance = async <T>(
    config: AxiosRequestConfig,
    options?: AxiosRequestConfig,
): Promise<T> => {
    return AXIOS_INSTANCE( {
        ...config,
        ...options,
        withCredentials: true,
        withXSRFToken: config.method !== "GET",
    } ).then( ( { data } ) => data );
};

export type ErrorType<Error> = AxiosError<Error>;