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
import ModalHeader from "@/components/custom-ui/ModalHeader";
import UserUpdateEmployeeForm from "@/components/admin/users/UserUpdateEmployeeForm";

function UpdateEmployeeModal() {
    const { id } = useParams<{ id: string }>();
    const { data: userDetails } = useGetUserByIdSuspense( id );
    return <ModalHeader title={ "Update Employee" }
                        description={ "Update the connected employee of this user." }
                        entity={ "user" }>
        <UserUpdateEmployeeForm user={ userDetails }></UserUpdateEmployeeForm>
    </ModalHeader>
}

export default dynamic( () => Promise.resolve( UpdateEmployeeModal ), {
    ssr: false,
} );