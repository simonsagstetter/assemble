/*
 * assemble
 * user-store.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";
import { User, UserRolesItem } from "@/api/rest/generated/fetch/openAPIDefinition.schemas";
import { createContext, ReactNode, useMemo, useState } from "react";

type UserContext = {
    user: User;
    isExternal: boolean;
    isUser: boolean;
    isManager: boolean;
    isAdmin: boolean;
    isSuperUser: boolean;
}


export const UserContext = createContext<UserContext | null>( null );


type UserProviderProps = {
    userDetails: User
    children: Readonly<ReactNode>
}

export default function UserProvider( { userDetails, children }: Readonly<UserProviderProps> ) {
    const [ state ] = useState<UserContext>( {
        user: userDetails,
        isExternal: userDetails.roles.includes( UserRolesItem.EXTERNAL ),
        isUser: userDetails.roles.includes( UserRolesItem.USER ),
        isManager: userDetails.roles.includes( UserRolesItem.MANAGER ),
        isAdmin: userDetails.roles.includes( UserRolesItem.ADMIN ),
        isSuperUser: userDetails.roles.includes( UserRolesItem.SUPERUSER ),
    } );

    const value = useMemo( () => (
            { ...state }
        ),
        [ state ]
    );

    return <UserContext.Provider value={ value }>{ children }</UserContext.Provider>
}