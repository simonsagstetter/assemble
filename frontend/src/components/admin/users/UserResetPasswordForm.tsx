/*
 * assemble
 * UserResetPasswordForm.tsx
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
import { useUpdateUserPassword } from "@/api/rest/generated/query/user-management/user-management";
import { UserResetPasswordFormData, UserResetPasswordSchema } from "@/types/users/user.types";
import { zodResolver } from "@hookform/resolvers/zod";
import { ScrollArea } from "@radix-ui/react-scroll-area";
import {
    FieldGroup,
    FieldSet
} from "@/components/ui/field";
import { Separator } from "@/components/ui/separator";
import { getGetUserSessionDetailsQueryKey } from "@/api/rest/generated/query/session-management/session-management";
import { toast } from "sonner";
import useModalContext from "@/hooks/useModalContext";
import { InputField, SwitchField } from "@/components/custom-ui/form/fields";
import { ErrorMessage, SuccessMessage } from "@/components/custom-ui/form/messages";
import { FormActionContext } from "@/store/formActionStore";
import { FormActions } from "@/components/custom-ui/form/actions";

export type UserResetPasswordFormProps = {
    user: UserAdmin
}

export default function UserResetPasswordForm( { user }: UserResetPasswordFormProps ) {
    const modalContext = useModalContext();
    const router = useRouter();
    const queryClient = useQueryClient();
    const form = useForm<UserResetPasswordFormData>( {
        resolver: zodResolver( UserResetPasswordSchema ),
        defaultValues: {
            newPassword: "",
            invalidateAllSessions: false
        }
    } );
    const { isSubmitting } = form.formState;

    const { mutate, isPending, isSuccess, isError } = useUpdateUserPassword();

    const handleUpdateUserPassword = ( data: UserResetPasswordFormData ) => {
        mutate(
            {
                id: user.id,
                data: {
                    newPassword: data.newPassword,
                    invalidateAllSessions: data.invalidateAllSessions
                }
            },
            {
                onSuccess: async () => {
                    form.clearErrors();
                    await queryClient.invalidateQueries( {
                        queryKey: getGetUserSessionDetailsQueryKey( user.id ),
                    } );
                    toast.success( "Success", {
                        description: "Password was reset"
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
                                form.setError( error.fieldName as keyof UserResetPasswordFormData, {
                                    type: "manual",
                                    message: error.errorMessage
                                } )
                            } )
                        }
                        if ( error.status === 404 && "message" in data ) {
                            form.setError( "root", { type: "manual", message: data.message } );

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
        value={ { isPending, isError, isSuccess, handleCancel, disableOnSuccess: false } }>
        <FormProvider { ...form }>
            <form id={ "user-reset-password-form" } onSubmit={ form.handleSubmit( handleUpdateUserPassword ) }
                  className="space-y-8">
                <ScrollArea className={ `${ modalContext ? "my-0" : "" }` }>
                    <FieldGroup className={ "py-4 px-8" }>
                        <FieldSet>
                            <FieldGroup>
                                <InputField fieldName={ "newPassword" }
                                            formControl={ form.control }
                                            label={ "New Password" }
                                            placeholder={ "Set a new password..." }
                                            type={ "password" }
                                            autoComplete={ "new-password" }
                                            disabled={ isPending || isSubmitting || isSuccess }
                                >
                                    This field is required.<br/><br/>
                                    Password Requirements:<br/>
                                    - Between 8 and 20 characters<br/>
                                    - At least one digit<br/>
                                    - At least one lowercase letter<br/>
                                    - At least one uppercase letter<br/>
                                    - At least one special character
                                </InputField>
                                <SwitchField fieldName={ "invalidateAllSessions" }
                                             formControl={ form.control }
                                             label={ "Session Invalidation" }
                                             disabled={ isPending || isSubmitting || isSuccess }>
                                    If this is enabled the user will be logged out of all sessions after the
                                    password was reset.
                                </SwitchField>
                            </FieldGroup>
                        </FieldSet>
                        <ErrorMessage/>
                        <SuccessMessage message={ "Password was reset successfully" }/>
                    </FieldGroup>
                </ScrollArea>
                <Separator className={ "my-0" }/>
                <FormActions formId={ "user-reset-password-form" } label={ "Save" }/>
            </form>
        </FormProvider>
    </FormActionContext.Provider>
}
