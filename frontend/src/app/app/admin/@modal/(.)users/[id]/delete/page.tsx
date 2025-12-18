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
import dynamic from "next/dynamic";
import { useParams } from "next/navigation";
import { useGetUserByIdSuspense } from "@/api/rest/generated/query/user-management/user-management";
import ModalHeader from "@/components/custom-ui/ModalHeader";
import UserDeleteForm from "@/components/admin/users/UserDeleteForm";

function DeleteUserModal() {
    const { id } = useParams<{ id: string }>();
    const { data: userDetails } = useGetUserByIdSuspense( id );

    return <ModalHeader title={ "Delete" } description={ "Confirm the action below to delete this user." }
                        entity={ "user" }>
        <UserDeleteForm user={ userDetails } modal/>
    </ModalHeader>
}

export default dynamic( () => Promise.resolve( DeleteUserModal ), {
    ssr: false,
} );
