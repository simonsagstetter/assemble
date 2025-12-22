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
import UserUpdateRolesForm from "@/components/admin/users/UserUpdateRolesForm";

function UpdateRolesModal() {
    const { id } = useParams<{ id: string }>();
    const { data: userDetails } = useGetUserByIdSuspense( id );
    return <ModalHeader title={ "Update Roles" }
                        description={ "Configure the authorization roles for this user." }
                        entity={ "user" }>
        <UserUpdateRolesForm user={ userDetails }></UserUpdateRolesForm>
    </ModalHeader>
}

export default dynamic( () => Promise.resolve( UpdateRolesModal ), {
    ssr: false,
} );