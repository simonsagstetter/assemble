/*
 * assemble
 * UserEditForm.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { type UserUpdateFormData, UserUpdateSchema } from "@/types/users/user.types";
import { useUpdateUser } from "@/api/rest/generated/query/user-management/user-management";
import { FieldSet } from "@/components/ui/field";
import { UserAdmin } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { useRouter } from "@bprogress/next/app";
import { IdentityFragment } from "@/components/users/form/fragments";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { getUpdateUserInvalidations } from "@/components/users/form/invalidations";
import { FormBuilder } from "@/components/custom-ui/form/form";

type UserEditFormProps = {
    user: UserAdmin,
}

export default function UserEditForm( { user }: Readonly<UserEditFormProps> ) {
    const { username, firstname, lastname, email } = user;
    const router = useRouter();

    const form = useForm<UserUpdateFormData>( {
        resolver: zodResolver( UserUpdateSchema ),
        defaultValues: {
            username,
            firstname,
            lastname,
            email,
        }
    } );

    const { isPending, isError, isSuccess, mutate } = useUpdateUser();

    const submitHandler = useFormHandler( {
        mutate,
        form,
        variables: ( data ) => ( {
            id: user.id,
            data: {
                username: data.username.toLowerCase(),
                firstname: data.firstname,
                lastname: data.lastname,
                email: data.email.toLowerCase()
            }
        } ),
        setInvalidations: ( data ) => getUpdateUserInvalidations( data ),
        onSuccessCallbackAction: () => router.back(),
        setToastSuccessOptions: ( data ) => ( {
            description: "User " + data.username + " was updated",
            action: {
                label: "View User",
                onClick: () => router.push( "/app/admin/users/" + data.id )
            }
        } )
    } )

    return <FormBuilder form={ form }
                        formId={ "edit-user-form" }
                        status={ { isPending, isSuccess, isError } }
                        submitHandler={ submitHandler }
                        cancelHandler={ () => router.back() }
                        options={ {
                            actionLabel: "Save",
                            successMessage: "User was updated successfully",
                        } }
    >
        <FieldSet>
            <IdentityFragment/>
        </FieldSet>
    </FormBuilder>
}
