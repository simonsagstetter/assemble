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
import UserUpdateEmployeeForm from "@/components/admin/users/UserUpdateEmployeeForm";

function UpdateEmployeePage() {
    const { id } = useParams<{ id: string }>();
    const { data: userDetails } = useGetUserByIdSuspense( id );

    return <FormPageHeader title={ "Update Employee" }
                           description={ "Update the connected employee of this user." }
                           entity={ "user" }>
        <UserUpdateEmployeeForm user={ userDetails }></UserUpdateEmployeeForm>
    </FormPageHeader>
}

export default dynamic( () => Promise.resolve( UpdateEmployeePage ), {
    ssr: false,
} );
