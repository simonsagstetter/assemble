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
import ModalHeader from "@/components/custom-ui/ModalHeader";
import UserUpdateStatusForm from "@/components/admin/users/UserUpdateStatusForm";
import { useParams } from "next/navigation";
import { useGetUserByIdSuspense } from "@/api/rest/generated/query/user-management/user-management";
import dynamic from "next/dynamic";

function UserStatusModal() {
    const { id } = useParams<{ id: string }>();
    const { data: userDetails } = useGetUserByIdSuspense( id );
    return <ModalHeader title={ "Update Status" } description={ "Update the status of the user." } entity={ "user" }>
        <UserUpdateStatusForm user={ userDetails } modal/>
    </ModalHeader>
}

export default dynamic( () => Promise.resolve( UserStatusModal ), {
    ssr: false,
} );