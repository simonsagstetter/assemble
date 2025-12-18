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

import { Field, FieldGroup } from "@/components/ui/field";
import { Separator } from "@/components/ui/separator";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { AlertCircleIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Spinner } from "@/components/ui/spinner";
import { useForm } from "react-hook-form";
import {
    getGetAllUsersQueryKey,
    getGetUserByIdQueryKey,
    useDeleteUserById
} from "@/api/rest/generated/query/user-management/user-management";
import { UserAdmin } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { useRouter } from "@bprogress/next/app";
import { toast } from "sonner";
import { useQueryClient } from "@tanstack/react-query";
import { use } from "react";
import { ModalContext } from "@/components/custom-ui/Modal";

type UserDeleteFormProps = {
    user: UserAdmin,
    modal?: boolean
}

export default function UserDeleteForm( { user, modal }: UserDeleteFormProps ) {
    const modalContext = use( ModalContext );
    const queryClient = useQueryClient();
    const router = useRouter();
    const form = useForm();
    const { isSubmitting, errors } = form.formState;
    const deleteUser = useDeleteUserById();
    const { isPending, isError, isSuccess } = deleteUser;

    const handleDeleteUser = () => {
        deleteUser.mutate(
            {
                id: user.id
            },
            {
                onSuccess: async () => {
                    toast.success( "Success", {
                        description: "User " + user.username + " was deleted",
                    } )
                    await queryClient.invalidateQueries(
                        {
                            queryKey:
                                getGetAllUsersQueryKey(),
                            refetchType: "all"

                        }
                    )
                    await queryClient.invalidateQueries(
                        {
                            queryKey:
                                getGetUserByIdQueryKey( user.id ),
                            refetchType: "none"
                        }
                    );
                    if ( modal ) {
                        modalContext.setOpen( false );
                    }
                    setTimeout( () => router.push( "/app/admin/users" ), 200 );
                    router.back();
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
        if ( modal ) {
            modalContext.setOpen( false );
            router.back();
        }
        router.back();
    }

    return <form id={ "user-delete-form" } onSubmit={ form.handleSubmit( handleDeleteUser ) } className="space-y-8">
        <FieldGroup className={ "p-8 my-0" }>
            <p className={ "text-xl font-semibold text-center" }>
                Are you sure you want to delete the user <strong>{ user.username }</strong>?
            </p>
            { isError && errors.root && (
                <FieldGroup>
                    <Alert variant="destructive">
                        <AlertCircleIcon/>
                        <AlertTitle>Oops! We encountered an error.</AlertTitle>
                        <AlertDescription>
                            { errors.root.message }
                        </AlertDescription>
                    </Alert>
                </FieldGroup>
            ) }
        </FieldGroup>
        <Separator className={ "my-0" }/>
        <Field orientation={ "horizontal" } className={ "p-8" }>
            <Button type="button"
                    variant="secondary"
                    className={ "flex-2/3 grow cursor-pointer" }
                    onClick={ handleCancel }
                    disabled={ isPending || isSubmitting || isSuccess }
            >
                Cancel
            </Button>
            <Button type="submit"
                    form={ "user-delete-form" }
                    variant="destructive"
                    disabled={ isPending || isSubmitting || isSuccess }
                    className={ "flex-1/3 grow cursor-pointer" }
            >
                { isPending ?
                    <>
                        { "Processing" }
                        <Spinner/>
                    </>
                    : "Delete"
                }
            </Button>
        </Field>
    </form>;
}
