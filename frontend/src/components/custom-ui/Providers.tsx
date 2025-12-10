/*
 * assemble
 * Providers.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";

import { ReactNode } from "react";
import { AppProgressProvider } from "@bprogress/next";
import { isServer, QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryStreamedHydration } from "@tanstack/react-query-next-experimental";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";

type ProvidersProps = {
    children: Readonly<ReactNode>
}

function makeQueryClient() {
    return new QueryClient( {
        defaultOptions: {
            queries: {
                staleTime: 1000 * 60 * 5
            },
        },
    } )
}

let browserQueryClient: QueryClient | undefined = undefined;

function getQueryClient() {
    if ( isServer ) {
        return makeQueryClient();
    } else {
        if ( !browserQueryClient ) browserQueryClient = makeQueryClient();
        return browserQueryClient;
    }
}

export default function Providers( { children }: ProvidersProps ) {
    const queryClient = getQueryClient();
    return <AppProgressProvider
        height={ "4px" }
        color={ "oklch(14.1% 0.005 285.823)" }
        options={ {
            showSpinner: false,
        } }
        shallowRouting
        disableSameURL
        startOnLoad
    >
        <QueryClientProvider client={ queryClient }>
            <ReactQueryStreamedHydration>
                { children }
            </ReactQueryStreamedHydration>
            <ReactQueryDevtools/>
        </QueryClientProvider>
    </AppProgressProvider>
}