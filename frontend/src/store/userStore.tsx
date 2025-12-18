/*
 * assemble
 * userStore.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";
import { User, UserRolesItem } from "@/api/rest/generated/fetch/openAPIDefinition.schemas";
import { createContext, ReactNode, useCallback, useReducer } from "react";


type UserState = {
    user: User | null;
    isExternal: boolean;
    isUser: boolean;
    isManager: boolean;
    isAdmin: boolean;
    isSuperUser: boolean;
}

const initialUserState: UserState = {
    user: null,
    isExternal: false,
    isUser: false,
    isManager: false,
    isAdmin: false,
    isSuperUser: false,
}

enum DispatchActionKind {
    SET,
}

type DispatchAction = | {
    type: DispatchActionKind.SET, payload: User
}

interface UserContext extends UserState {
    setState: ( user: User ) => void;
}

export const UserContext = createContext<UserContext>( {
    ...initialUserState,
    setState: () => {
    },
} );

function stateReducer( state: UserState, action: DispatchAction ) {
    if ( action.type === DispatchActionKind.SET ) {
        return {
            ...state,
            user: action.payload,
            isExternal: action.payload.roles.includes( UserRolesItem.EXTERNAL ),
            isUser: action.payload.roles.includes( UserRolesItem.USER ),
            isManager: action.payload.roles.includes( UserRolesItem.MANAGER ),
            isAdmin: action.payload.roles.includes( UserRolesItem.ADMIN ),
            isSuperUser: action.payload.roles.includes( UserRolesItem.SUPERUSER ),
        }
    }
    return state;
}

export default function UserProvider( { children }: { children: Readonly<ReactNode> } ) {
    const [ state, dispatch ] = useReducer( stateReducer, initialUserState );

    const setState = useCallback( ( user: User ) => dispatch( { type: DispatchActionKind.SET, payload: user } ), [] );


    const value = { ...state, setState };

    return <UserContext.Provider value={ value }>{ children }</UserContext.Provider>
}