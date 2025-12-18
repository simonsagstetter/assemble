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
import { use } from "react";
import { ModalContext } from "@/components/custom-ui/Modal";
import { useRouter } from "@bprogress/next/app";
import { useQueryClient } from "@tanstack/react-query";
import { Controller, useForm } from "react-hook-form";
import { useUpdateUserPassword } from "@/api/rest/generated/query/user-management/user-management";
import { UserResetPasswordFormData, UserResetPasswordSchema } from "@/types/users/user.types";
import { zodResolver } from "@hookform/resolvers/zod";
import { ScrollArea } from "@radix-ui/react-scroll-area";
import {
    Field,
    FieldContent,
    FieldDescription,
    FieldError,
    FieldGroup,
    FieldLabel,
    FieldSet
} from "@/components/ui/field";
import { Switch } from "@/components/ui/switch";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { AlertCircleIcon, CheckCircle2Icon } from "lucide-react";
import { Separator } from "@/components/ui/separator";
import { Button } from "@/components/ui/button";
import { Spinner } from "@/components/ui/spinner";
import { Input } from "@/components/ui/input";
import { getGetUserSessionDetailsQueryKey } from "@/api/rest/generated/query/session-management/session-management";
import { toast } from "sonner";

export type UserResetPasswordFormProps = {
    user: UserAdmin,
    modal?: boolean
}

export default function UserResetPasswordForm( { user, modal = false }: UserResetPasswordFormProps ) {
    const modalContext = use( ModalContext );
    const router = useRouter();
    const queryClient = useQueryClient();
    const form = useForm<UserResetPasswordFormData>( {
        resolver: zodResolver( UserResetPasswordSchema ),
        defaultValues: {
            newPassword: "",
            invalidateAllSessions: false
        }
    } );
    const { errors, isSubmitting } = form.formState;

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
                    if ( modal ) {
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
        if ( modal ) {
            modalContext.setOpen( false );
            router.back();
        } else {
            if ( !isSuccess ) router.back();
            else router.replace( "/app/admin/users" );
        }
    }
    return <form id={ "user-rest-password-form" } onSubmit={ form.handleSubmit( handleUpdateUserPassword ) }
                 className="space-y-8">
        <ScrollArea className={ `${ modal ? "my-0" : "" }` }>
            <FieldGroup className={ "py-4 px-8" }>
                <FieldSet>
                    <FieldGroup>
                        <Controller
                            name={ "newPassword" }
                            control={ form.control }
                            render={ ( { field, fieldState } ) => (
                                <Field data-invalid={ fieldState.invalid }>
                                    <FieldLabel htmlFor={ "password-field" }>New Password</FieldLabel>
                                    <Input
                                        { ...field }
                                        id={ "password-field" }
                                        aria-invalid={ fieldState.invalid }
                                        placeholder="Set a new password..."
                                        type={ "password" }
                                        autoComplete={ "new-password" }
                                        disabled={ isPending || isSubmitting || isSuccess }
                                    />
                                    <FieldDescription>
                                        This field is required.<br/><br/>
                                        Passwort Requirements:<br/>
                                        - Between 8 and 20 characters<br/>
                                        - At least one digit<br/>
                                        - At least one lowercase letter<br/>
                                        - At least one uppercase letter<br/>
                                        - At least one special character
                                    </FieldDescription>
                                    { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                                    </FieldError> }
                                </Field>
                            ) }
                        />
                        <Controller
                            name={ "invalidateAllSessions" }
                            control={ form.control }
                            render={ ( { field, fieldState } ) => (
                                <Field data-invalid={ fieldState.invalid } orientation={ "horizontal" }>
                                    <FieldContent>
                                        <FieldLabel htmlFor={ "session-field" }>Session Invalidation</FieldLabel>
                                        <FieldDescription>
                                            If this is enabled the user will be logged out of all sessions after the
                                            password was reset.
                                        </FieldDescription>
                                        { fieldState.invalid &&
                                            <FieldError errors={ [ fieldState.error ] }>
                                            </FieldError>
                                        }

                                    </FieldContent>
                                    <Switch
                                        id={ "session-field" }
                                        className={ "cursor-pointer" }
                                        name={ field.name }
                                        checked={ field.value }
                                        onCheckedChange={ field.onChange }
                                        disabled={ isPending || isSubmitting || isSuccess }/>
                                </Field>
                            ) }
                        />
                    </FieldGroup>
                </FieldSet>
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
                { isSuccess && (
                    <FieldGroup>
                        <Alert variant="default">
                            <CheckCircle2Icon/>
                            <AlertTitle>
                                { "User was updated successfully" }
                            </AlertTitle>
                        </Alert>
                    </FieldGroup>
                ) }
            </FieldGroup>
        </ScrollArea>
        <Separator className={ "my-0" }/>
        <Field orientation={ "horizontal" } className={ "p-8" }>
            <Button type="button"
                    variant="secondary"
                    className={ "flex-1/3 grow cursor-pointer" }
                    onClick={ handleCancel }
                    disabled={ isPending || isSubmitting }
            >
                { isSuccess ? "Go back" : "Cancel" }
            </Button>
            <Button type="submit"
                    form={ "user-rest-password-form" }
                    variant="default"
                    disabled={ isPending || isSubmitting }
                    className={ "flex-2/3 grow cursor-pointer" }
            >
                { isPending ?
                    <>
                        { "Processing" }
                        <Spinner/>
                    </>
                    : "Save"
                }
            </Button>
        </Field>
    </form>
}
