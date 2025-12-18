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
import UserEditForm from "@/components/admin/users/UserEditForm";
import FormPageHeader from "@/components/custom-ui/FormPageHeader";

function EditUserPage() {
    const { id } = useParams<{ id: string }>();
    const { data: userDetails } = useGetUserByIdSuspense( id );

    return <FormPageHeader title={ "Edit" } description={ "Update the fields and click save to update the user." }
                           entity={ "User" }>
        <UserEditForm user={ userDetails }/>
    </FormPageHeader>
}

export default dynamic( () => Promise.resolve( EditUserPage ), {
    ssr: false,
} );
