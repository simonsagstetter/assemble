/*
 * assemble
 * RequireRole.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";

import { UserRolesItem } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import useUserContext from "@/hooks/use-user";
import { ReactNode } from "react";
import dynamic from "next/dynamic";

type RequireRoleProps = {
    roles: UserRolesItem[],
    children: Readonly<ReactNode>
}

function RequireRole( { roles, children }: RequireRoleProps ) {
    const ctx = useUserContext();

    if ( !ctx?.user?.roles.some( role => roles.includes( role ) ) )
        throw new Error( "Unauthorized" );

    return children;
}

export default dynamic( () => Promise.resolve( RequireRole ), {
    ssr: false,
} );