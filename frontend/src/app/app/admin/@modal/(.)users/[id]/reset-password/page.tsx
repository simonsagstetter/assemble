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
import UserResetPasswordForm from "@/components/admin/users/UserResetPasswordForm";

function ResetPasswordModal() {
    const { id } = useParams<{ id: string }>();
    const { data: userDetails } = useGetUserByIdSuspense( id );
    return <ModalHeader title={ "Reset Password" }
                        description={ "Set the password of a user and invalidate their session" }
                        entity={ "user" }>
        <UserResetPasswordForm user={ userDetails } modal></UserResetPasswordForm>
    </ModalHeader>
}

export default dynamic( () => Promise.resolve( ResetPasswordModal ), {
    ssr: false,
} );