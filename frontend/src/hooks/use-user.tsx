/*
 * assemble
 * use-user.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
import { use } from "react";
import { UserContext } from "@/store/userStore";

export default function useUserContext() {
    const ctx = use( UserContext );
    if ( !ctx ) {
        throw new Error( "useUser must be used within a UserProvider" );
    }

    return ctx;
}
