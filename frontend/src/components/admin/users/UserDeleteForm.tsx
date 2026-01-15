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

import { FieldGroup } from "@/components/ui/field";
import { Separator } from "@/components/ui/separator";
import { FormProvider, useForm } from "react-hook-form";
import {
    getGetAllUsersQueryKey,
    getGetUserByIdQueryKey,
    useDeleteUserById
} from "@/api/rest/generated/query/user-management/user-management";
import { UserAdmin } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { useRouter } from "@bprogress/next/app";
import { toast } from "sonner";
import { useQueryClient } from "@tanstack/react-query";
import { ErrorMessage } from "@/components/custom-ui/form/messages";
import { FormActionContext } from "@/store/formActionStore";
import { FormActions } from "@/components/custom-ui/form/actions";

type UserDeleteFormProps = {
    user: UserAdmin
}

export default function UserDeleteForm( { user }: UserDeleteFormProps ) {
    const queryClient = useQueryClient();
    const router = useRouter();
    const form = useForm();
    const deleteUser = useDeleteUserById();
    const { isPending, isError, isSuccess } = deleteUser;

    const handleDeleteUser = () => {
        deleteUser.mutate(
            {
                id: user.id
            },
            {
                onSuccess: async () => {
                    await queryClient.invalidateQueries( {
                        queryKey: getGetAllUsersQueryKey(),
                        refetchType: "all"
                    } )
                    await queryClient.invalidateQueries( {
                        queryKey: getGetUserByIdQueryKey( user.id ),
                        refetchType: "none"
                    } );
                    toast.success( "Success", {
                        description: "User was deleted",
                    } )
                    router.push( "/app/admin/users" );
                },
                onError: ( error ) => {
                    if ( error.response?.data ) {
                        const data = error.response.data;
                        if ( "message" in data && data.message ) {
                            form.setError( "root", { type: "manual", message: "User could not be deleted." } )
                        }
                    } else {
                        form.setError( "root", { type: "manual", message: "An unknown error occurred." } );
                    }
                }
            }
        )
    }

    const handleCancel = () => {
        router.back();
    }

    return <FormActionContext.Provider
        value={ { isSuccess, isPending, isError, handleCancel, disableOnSuccess: true } }>
        <FormProvider { ...form }>
            <form id={ "user-delete-form" } onSubmit={ form.handleSubmit( handleDeleteUser ) } className="space-y-8">
                <FieldGroup className={ "p-8 my-0" }>
                    <p className={ "text-xl font-semibold text-center" }>
                        Are you sure you want to delete the user <strong>{ user.username }</strong>?
                    </p>
                    <ErrorMessage/>
                </FieldGroup>
                <Separator className={ "my-0" }/>
                <FormActions formId={ "user-delete-form" } label={ "Delete" } variant={ "destructive" }/>
            </form>
        </FormProvider>
    </FormActionContext.Provider>
}
