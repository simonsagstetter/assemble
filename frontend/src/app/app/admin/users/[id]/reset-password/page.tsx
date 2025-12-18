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
import UserResetPasswordForm from "@/components/admin/users/UserResetPasswordForm";

function ResetPasswordPage() {
    const { id } = useParams<{ id: string }>();
    const { data: userDetails } = useGetUserByIdSuspense( id );

    return <FormPageHeader title={ "Reset Password" }
                           description={ "Set the password of a user and invalidate their session" }
                           entity={ "user" }>
        <UserResetPasswordForm user={ userDetails }/>
    </FormPageHeader>
}

export default dynamic( () => Promise.resolve( ResetPasswordPage ), {
    ssr: false,
} );
