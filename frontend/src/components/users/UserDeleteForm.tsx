/*
 * assemble
 * UserDeleteForm.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";

import { useForm } from "react-hook-form";
import { useDeleteUserById } from "@/api/rest/generated/query/user-management/user-management";
import { UserAdmin } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { useRouter } from "@bprogress/next/app";
import { ErrorMessage } from "@/components/custom-ui/form/messages";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { getDeleteUserInvalidations } from "@/components/users/form/invalidations";
import { FormBuilder } from "@/components/custom-ui/form/form";

type UserDeleteFormProps = {
    user: UserAdmin
}

export default function UserDeleteForm( { user }: Readonly<UserDeleteFormProps> ) {
    const router = useRouter();
    const form = useForm();
    const { isPending, isError, isSuccess, mutate } = useDeleteUserById();

    const submitHandler = useFormHandler( {
        mutate,
        form,
        variables: () => ( {
            id: user.id
        } ),
        setInvalidations: () => getDeleteUserInvalidations( user ),
        onSuccessCallbackAction: () => router.push( "/app/admin/users" ),
        setToastSuccessOptions: () => ( {
            description: "User deleted"
        } )
    } )

    return <FormBuilder form={ form }
                        formId={ "delete-user-form" }
                        status={ { isPending, isError, isSuccess } }
                        submitHandler={ submitHandler }
                        cancelHandler={ () => router.back() }
                        options={ {
                            actionLabel: "Delete",
                            disabledOnSuccess: true,
                            successMessage: "User was deleted successfully.",
                            actionVariant: "destructive"
                        } }
    >
        <p className={ "text-xl font-semibold text-center" }>
            Are you sure you want to delete the user <strong>{ user.username }</strong>?
        </p>
        <ErrorMessage/>
    </FormBuilder>
}
