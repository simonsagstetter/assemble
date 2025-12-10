/*
 * assemble
 * page.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client"

import AccountDetail from "@/components/account/AccountDetail";
import Loading from "@/components/custom-ui/Loading";
import { useMe } from "@/api/rest/generated/query/users/users";

export default function AccountPage() {
    const { data: userDetails, isLoading, isError, error } = useMe( { axios: { withCredentials: true } } );

    if ( isError ) {
        throw ( error instanceof Error ) ? error : new Error( "Failed to fetch user details" );
    }

    if ( isLoading || !userDetails || !userDetails.data ) return <Loading/>;

    return <div className="p-8">
        <AccountDetail userDetails={ userDetails.data }/>
    </div>
}
