/*
 * assemble
 * UserUpdateRolesForm.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";

import { FieldValidationError, UserAdmin } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { useRouter } from "@bprogress/next/app";
import { useQueryClient } from "@tanstack/react-query";
import { FormProvider, useForm } from "react-hook-form";
import {
    getGetAllUsersQueryKey,
    getGetUserByIdQueryKey,
    useUpdateUserRoles,
} from "@/api/rest/generated/query/user-management/user-management";
import { UserUpdateRolesFormData, UserUpdateRolesSchema } from "@/types/users/user.types";
import { zodResolver } from "@hookform/resolvers/zod";
import { ScrollArea } from "@radix-ui/react-scroll-area";
import { FieldGroup, FieldSet } from "@/components/ui/field";
import { Separator } from "@/components/ui/separator";
import { toast } from "sonner";
import { RolesLookupField } from "@/components/admin/users/form/custom-fields";
import useModalContext from "@/hooks/useModalContext";
import { FormActionContext } from "@/store/formActionStore";
import { ErrorMessage, SuccessMessage } from "@/components/custom-ui/form/messages";
import { FormActions } from "@/components/custom-ui/form/actions";

type UserUpdateRolesFormProps = {
    user: UserAdmin
}

export default function UserUpdateRolesForm( { user }: UserUpdateRolesFormProps ) {
    const modalContext = useModalContext();
    const router = useRouter();
    const queryClient = useQueryClient();
    const { roles } = user;
    const form = useForm<UserUpdateRolesFormData>( {
        resolver: zodResolver( UserUpdateRolesSchema ),
        defaultValues: {
            roles: [ ...roles ]
        }
    } );
    const { isSubmitting } = form.formState;
    const { mutate, isPending, isSuccess, isError } = useUpdateUserRoles();

    const handleUpdateRoles = ( data: UserUpdateRolesFormData ) => {
        mutate(
            {
                id: user.id,
                data: {
                    roles: data.roles
                }
            },
            {
                onSuccess: async ( user ) => {
                    form.clearErrors();
                    await queryClient.invalidateQueries( {
                        queryKey: getGetAllUsersQueryKey(),
                        refetchType: "all"
                    } )
                    await queryClient.invalidateQueries( {
                        queryKey: getGetUserByIdQueryKey( user.id ),
                        refetchType: "all"
                    } )
                    toast.success( "Success", {
                        description: "User " + user.username + " was updated",
                        action: {
                            label: "View User",
                            onClick: () => router.push( "/app/admin/users/" + user.id )
                        }
                    } );
                    if ( modalContext ) {
                        handleCancel();
                    }
                },
                onError: ( error ) => {
                    if ( error.response?.data ) {
                        const data = error.response.data;
                        if ( error.status === 400 && "errors" in data && data.errors ) {
                            data.errors.forEach( ( error: FieldValidationError ) => {
                                form.setError( error.fieldName as keyof UserUpdateRolesFormData, {
                                    type: "manual",
                                    message: error.errorMessage
                                } )
                            } )
                        }

                        if ( error.status === 404 && "message" in data && data.message ) {
                            form.setError( "root", { type: "manual", message: data.message } )
                        }
                    } else {
                        form.setError( "root", { type: "manual", message: "An unknown error occurred." } );
                    }
                }
            }
        )
    }

    const handleCancel = () => {
        if ( modalContext ) {
            modalContext.setOpen( false );
            router.back();
        } else {
            if ( !isSuccess ) router.back();
            else router.replace( "/app/admin/users" );
        }
    }

    return <FormActionContext.Provider
        value={ { isError, isSuccess, isPending, handleCancel, disableOnSuccess: false } }>
        <FormProvider { ...form }>
            <form id={ "user-status-form" } onSubmit={ form.handleSubmit( handleUpdateRoles ) } className="space-y-8">
                <ScrollArea className={ `${ modalContext ? "my-0" : "" }` }>
                    <FieldGroup className={ "py-4 px-8" }>
                        <FieldSet>
                            <FieldGroup>
                                <RolesLookupField fieldName={ "roles" } formControl={ form.control }
                                                  disabled={ isPending || isSubmitting || isSuccess }/>
                            </FieldGroup>
                        </FieldSet>
                        <ErrorMessage/>
                        <SuccessMessage message={ "User was updated successfully" }/>
                    </FieldGroup>
                </ScrollArea>
                <Separator className={ "my-0" }/>
                <FormActions formId={ "user-status-form" } label={ "Save" }/>
            </form>
        </FormProvider>
    </FormActionContext.Provider>
}
