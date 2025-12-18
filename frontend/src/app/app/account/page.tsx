/*
 * assemble
 * page.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";
import { useMeSuspense } from "@/api/rest/generated/query/users/users";
import dynamic from "next/dynamic";
import AccountDetail from "@/components/account/AccountDetail";

function AccountPage() {
    const { data } = useMeSuspense( {
        query: {
            staleTime: 0
        }
    } );


    return <div className="px-4 py-8">
        <AccountDetail userDetails={ data }/>
    </div>
}

export default dynamic( () => Promise.resolve( AccountPage ), {
    ssr: false,
} );