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
import { useGetAllUsersSuspense } from "@/api/rest/generated/query/user-management/user-management";
import dynamic from "next/dynamic";
import UserDataTable from "@/components/admin/users/UserDataTable";

function UsersPage() {
    const { data: users } = useGetAllUsersSuspense();

    return <UserDataTable users={ users }/>
}

export default dynamic( () => Promise.resolve( UsersPage ), {
    ssr: false,
} );
