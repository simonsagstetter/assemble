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
    const { data: userDetails, isLoading, isError } = useMe( { axios: { withCredentials: true } } );

    if ( isLoading || !userDetails ) return <Loading/>;

    if ( isError ) throw new Error( "Failed to fetch user details" );

    return <div className="p-8">
        <AccountDetail userDetails={ userDetails.data }/>
    </div>
}
