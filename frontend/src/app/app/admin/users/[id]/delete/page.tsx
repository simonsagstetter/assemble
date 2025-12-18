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

import { useParams } from "next/navigation";
import { useGetUserByIdSuspense } from "@/api/rest/generated/query/user-management/user-management";
import dynamic from "next/dynamic";
import UserDeleteForm from "@/components/admin/users/UserDeleteForm";
import FormPageHeader from "@/components/custom-ui/FormPageHeader";

function DeleteUserPage() {
    const { id } = useParams<{ id: string }>();
    const { data: userDetails } = useGetUserByIdSuspense( id );
    return <FormPageHeader title={ "Delete" } description={ "Confirm the action below to delete this user." }
                           entity={ "user" }>
        <UserDeleteForm user={ userDetails }/>
    </FormPageHeader>

}

export default dynamic( () => Promise.resolve( DeleteUserPage ), {
    ssr: false,
} );