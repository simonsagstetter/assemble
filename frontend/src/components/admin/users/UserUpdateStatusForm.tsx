/*
 * assemble
 * UserUpdateStatusForm.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";

import { UserAdmin } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { useRouter } from "@bprogress/next/app";
import { useQueryClient } from "@tanstack/react-query";
import { FormProvider, useForm } from "react-hook-form";
import {
    getGetAllUsersQueryKey,
    getGetUserByIdQueryKey,
    useUpdateUserStatus
} from "@/api/rest/generated/query/user-management/user-management";
import { type UserStatusFormData, UserStatusSchema } from "@/types/users/user.types";
import { zodResolver } from "@hookform/resolvers/zod";
import { ScrollArea } from "@radix-ui/react-scroll-area";
import {
    FieldGroup,
    FieldSet
} from "@/components/ui/field";
import { Separator } from "@/components/ui/separator";
import { toast } from "sonner";
import { SwitchField } from "@/components/custom-ui/form/fields";
import { ErrorMessage, SuccessMessage } from "@/components/custom-ui/form/messages";
import { FormActions } from "@/components/custom-ui/form/actions";
import { FormActionContext } from "@/store/formActionStore";
import useModalContext from "@/hooks/useModalContext";

type UserStatusFormProps = {
    user: UserAdmin
}

export default function UserUpdateStatusForm( { user }: UserStatusFormProps ) {
    const modalContext = useModalContext();
    const router = useRouter();
    const queryClient = useQueryClient();
    const { enabled, locked } = user;
    const form = useForm<UserStatusFormData>( {
        resolver: zodResolver( UserStatusSchema ),
        defaultValues: {
            enabled,
            locked
        }
    } );
    const { isSubmitting } = form.formState;
    const { mutate, isPending, isSuccess, isError } = useUpdateUserStatus();

    const handleUpdateStatus = ( data: UserStatusFormData ) => {
        mutate(
            {
                id: user.id,
                data: {
                    enabled: data.enabled,
                    locked: data.locked
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
                    if ( error.status === 404 && error.response?.data ) {
                        const data = error.response.data;
                        form.setError( "root", { type: "manual", message: data.message } );
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

    return <FormProvider { ...form }>
        <FormActionContext.Provider value={ { isPending, isSuccess, isError, handleCancel, disableOnSuccess: false } }>
            <form id={ "user-status-form" } onSubmit={ form.handleSubmit( handleUpdateStatus ) } className="space-y-8">
                <ScrollArea className={ `${ modalContext ? "my-0" : "" }` }>
                    <FieldGroup className={ "py-4 px-8" }>
                        <FieldSet>
                            <FieldGroup>

                                <SwitchField fieldName={ "enabled" }
                                             formControl={ form.control }
                                             label={ "Enabled Status" }
                                             disabled={ isPending || isSubmitting || isSuccess }>
                                    Enable or disable the new user account
                                </SwitchField>
                                <SwitchField fieldName={ "locked" }
                                             formControl={ form.control }
                                             label={ "Locked Status" }
                                             disabled={ isPending || isSubmitting || isSuccess }>
                                    Locked users cannot log in but can still be connected to data
                                </SwitchField>
                            </FieldGroup>
                        </FieldSet>
                        <ErrorMessage/>
                        <SuccessMessage message={ "User was updated successfully" }/>
                    </FieldGroup>
                </ScrollArea>
                <Separator className={ "my-0" }/>
                <FormActions formId={ "user-status-form" } label={ "Save" }/>
            </form>
        </FormActionContext.Provider>
    </FormProvider>

}
