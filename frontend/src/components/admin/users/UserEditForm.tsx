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
import { useQueryClient } from "@tanstack/react-query";
import { FormProvider, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { type UserUpdateFormData, UserUpdateSchema } from "@/types/users/user.types";
import {
    getGetAllUsersQueryKey,
    getGetUserByIdQueryKey,
    useUpdateUser
} from "@/api/rest/generated/query/user-management/user-management";
import { FieldGroup, FieldSet } from "@/components/ui/field";
import { Separator } from "@/components/ui/separator";
import { toast } from "sonner";
import { FieldValidationError, UserAdmin } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { useRouter } from "@bprogress/next/app";
import { ScrollArea } from "@radix-ui/react-scroll-area";
import { FormActionContext } from "@/store/formActionStore";
import useModalContext from "@/hooks/useModalContext";
import { FormActions } from "@/components/custom-ui/form/actions";
import { IdentityFragment } from "@/components/admin/users/form/fragments";
import { ErrorMessage, SuccessMessage } from "@/components/custom-ui/form/messages";

type UserEditFormProps = {
    user: UserAdmin,
}

export default function UserEditForm( { user }: UserEditFormProps ) {
    const modalContext = useModalContext();
    const { username, firstname, lastname, email } = user;
    const queryClient = useQueryClient();
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

    const updateUser = useUpdateUser();

    const { isPending, isError, isSuccess } = updateUser;


    const handleUpdateUser = ( data: UserUpdateFormData ) => {
        updateUser.mutate( {
                id: user.id,
                data: {
                    username: data.username.toLowerCase(),
                    firstname: data.firstname,
                    lastname: data.lastname,
                    email: data.email.toLowerCase()
                }
            },
            {
                onSuccess: async ( user ) => {
                    await queryClient.invalidateQueries( {
                        queryKey: getGetAllUsersQueryKey(),
                        refetchType: "all"
                    } )
                    await queryClient.invalidateQueries( {
                        queryKey: getGetUserByIdQueryKey( user.id ),
                        refetchType: "all"
                    } )
                    form.clearErrors();
                    toast.success( "Success", {
                        description: "User " + user.username + " was updated",
                        action: {
                            label: "View User",
                            onClick: () => router.push( "/app/admin/users/" + user.id )
                        }
                    } );
                    handleCancel();
                },
                onError: ( error ) => {
                    if ( error.response?.data ) {
                        const data = error.response.data;

                        if ( error.status === 400 && "errors" in data && data.errors ) {
                            data.errors.forEach( ( error: FieldValidationError ) => {
                                form.setError( error.fieldName as keyof UserUpdateFormData, {
                                    type: "manual",
                                    message: error.errorMessage
                                } )
                            } )
                        }

                        if ( error.status === 409 && "message" in data && data.message ) {
                            form.setError( "username", { type: "manual", message: "Username already exists." } )
                        }
                    } else {
                        form.setError( "root", { type: "manual", message: "An unknown error occurred." } );
                    }
                }
            } )
    }

    const handleCancel = () => {
        router.back();
    }

    return <FormActionContext.Provider
        value={ { isPending, isSuccess, isError, handleCancel, disableOnSuccess: false } }>
        <FormProvider { ...form }>
            <form id={ "user-edit-form" } onSubmit={ form.handleSubmit( handleUpdateUser ) } className="space-y-8">
                <ScrollArea className={ `${ modalContext ? "my-0" : "" }` }>
                    <FieldGroup className={ "py-4 px-8" }>
                        <FieldSet>
                            <IdentityFragment/>
                        </FieldSet>
                        <ErrorMessage/>
                        <SuccessMessage message={ "User was updated successfully" }/>
                    </FieldGroup>
                </ScrollArea>
                <Separator className={ "my-0" }/>
                <FormActions formId={ "user-edit-form" } label={ "Save" }/>
            </form>
        </FormProvider>
    </FormActionContext.Provider>
}
