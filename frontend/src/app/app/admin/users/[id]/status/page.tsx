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
import FormPageHeader from "@/components/custom-ui/FormPageHeader";
import UserUpdateStatusForm from "@/components/admin/users/UserUpdateStatusForm";

function UserStatusPage() {
    const { id } = useParams<{ id: string }>();
    const { data: userDetails } = useGetUserByIdSuspense( id );

    return <FormPageHeader title={ "Update Status" } description={ "Update the status of the user." } entity={ "user" }>
        <UserUpdateStatusForm user={ userDetails }/>
    </FormPageHeader>
}

export default dynamic( () => Promise.resolve( UserStatusPage ), {
    ssr: false,
} );
