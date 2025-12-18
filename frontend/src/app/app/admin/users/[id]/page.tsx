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
import { useParams } from "next/navigation";
import { useGetUserByIdSuspense } from "@/api/rest/generated/query/user-management/user-management";
import UserDetail from "@/components/admin/users/UserDetail";
import dynamic from "next/dynamic";

function UserDetailPage() {
    const { id } = useParams<{ id: string }>();
    const { data: userDetails } = useGetUserByIdSuspense( id );
    return <UserDetail userDetails={ userDetails }/>;
}

export default dynamic( () => Promise.resolve( UserDetailPage ), {
    ssr: false,
} );
