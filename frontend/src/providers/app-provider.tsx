/*
 * assemble
 * global-provider.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";

import { ReactNode } from "react";
import { isServer, QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryStreamedHydration } from "@tanstack/react-query-next-experimental";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import UserProvider from "@/store/user-store";
import { User } from "@/api/rest/generated/fetch/openAPIDefinition.schemas";

type ProvidersProps = {
    userDetails: User
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
        browserQueryClient ??= makeQueryClient();
        return browserQueryClient;
    }
}

export default function AppProvider( { userDetails, children }: Readonly<ProvidersProps> ) {
    const queryClient = getQueryClient();
    return <QueryClientProvider client={ queryClient }>
        <ReactQueryStreamedHydration>
            <UserProvider userDetails={ userDetails }>
                { children }
            </UserProvider>
        </ReactQueryStreamedHydration>
        <ReactQueryDevtools/>
    </QueryClientProvider>
}