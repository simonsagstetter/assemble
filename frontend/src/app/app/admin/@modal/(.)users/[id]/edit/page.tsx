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
import UserEditForm from "@/components/admin/users/UserEditForm";
import dynamic from "next/dynamic";
import { useParams } from "next/navigation";
import { useGetUserByIdSuspense } from "@/api/rest/generated/query/user-management/user-management";
import ModalHeader from "@/components/custom-ui/ModalHeader";

function EditUserModal() {
    const { id } = useParams<{ id: string }>();
    const { data: userDetails } = useGetUserByIdSuspense( id );

    return <ModalHeader title={ "Edit" } description={ "Update the fields and click save to update the user." }
                        entity={ "User" }>
        <UserEditForm user={ userDetails } modal/>
    </ModalHeader>
}

export default dynamic( () => Promise.resolve( EditUserModal ), {
    ssr: false,
} );
