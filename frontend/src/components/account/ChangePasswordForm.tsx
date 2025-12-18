/*
 * assemble
 * ChangePasswordForm.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";

import {
    Field,
    FieldDescription,
    FieldError,
    FieldGroup,
    FieldLabel,
    FieldLegend,
    FieldSet
} from "@/components/ui/field";
import { Controller, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { ChangePasswordFormData, ChangePasswordSchema } from "@/types/auth/auth.types";
import { ChangePassword, FieldValidationError, } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { Input } from "@/components/ui/input";
import { toast } from "sonner";
import { Spinner } from "@/components/ui/spinner";
import { Button } from "@/components/ui/button";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { AlertCircleIcon, CheckCircle2Icon } from "lucide-react";
import { useChangePassword } from "@/api/rest/generated/query/users/users";

export default function ChangePasswordForm() {
    const form = useForm<ChangePasswordFormData>( {
        resolver: zodResolver( ChangePasswordSchema ),
        defaultValues: {
            oldPassword: "",
            newPassword: "",
            confirmPassword: ""
        }
    } );

    const { isSubmitting, isValid, errors } = form.formState;

    const changePassword = useChangePassword();

    const { isPending, isError, isSuccess } = changePassword;

    const onSubmit = ( data: ChangePasswordFormData & { confirmPassword: string } ) => {
        const { oldPassword, newPassword, confirmPassword } = data;
        if ( newPassword !== confirmPassword ) {
            form.setError( "confirmPassword", { message: "Passwords do not match", type: "manual" } );
            return;
        }
        changePassword.mutate(
            {
                data: {
                    oldPassword, newPassword
                },
            },
            {
                onSuccess: () => {
                    form.reset( { oldPassword: "", newPassword: "", confirmPassword: "" } );
                    form.clearErrors();
                    toast.success( "Success", {
                        description: "Your password was changed",
                    } );
                },
                onError: ( error ) => {
                    if ( error.status === 400 && error.response?.data ) {
                        const data = error.response?.data;

                        if ( "errors" in data && data.errors ) {
                            data.errors.forEach( ( error: FieldValidationError ) => {
                                form.setError( error.fieldName as keyof ChangePassword, {
                                    type: "manual",
                                    message: error.errorMessage
                                } )
                            } )
                        } else if ( "message" in data && data.message ) {
                            form.setError( "oldPassword", { type: "manual", message: data.message } )
                        }
                    } else {
                        form.setError( "root", { type: "manual", message: "An unknown error occurred." } );
                    }
                }
            }
        );
    }

    return (
        <form onSubmit={ form.handleSubmit( onSubmit ) }>
            <FieldSet>
                <FieldLegend>Change Password</FieldLegend>
                <FieldDescription>Change your password for this application.</FieldDescription>
                <FieldGroup>
                    <Controller
                        name={ "oldPassword" }
                        control={ form.control }
                        render={ ( { field, fieldState } ) => (
                            <Field data-invalid={ fieldState.invalid }>
                                <FieldLabel htmlFor={ "oldPassword-field" }>Old password</FieldLabel>
                                <Input
                                    { ...field }
                                    id={ "oldPassword-field" }
                                    aria-invalid={ fieldState.invalid }
                                    placeholder="Type in your current password"
                                    type={ "password" }
                                    autoComplete={ "current-password" }
                                    disabled={ isSubmitting || isPending }
                                />
                                { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                                </FieldError> }
                            </Field>
                        ) }
                    />
                    <Controller
                        name={ "newPassword" }
                        control={ form.control }
                        render={ ( { field, fieldState } ) => (
                            <Field data-invalid={ fieldState.invalid }>
                                <FieldLabel htmlFor={ "newPassword-field" }>New password</FieldLabel>
                                <Input
                                    { ...field }
                                    id={ "newPassword-field" }
                                    aria-invalid={ fieldState.invalid }
                                    placeholder="Type in your new password"
                                    type={ "password" }
                                    autoComplete={ "new-password" }
                                    disabled={ isSubmitting || isPending }
                                />
                                { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                                </FieldError> }
                            </Field>
                        ) }
                    />
                    <Controller
                        name={ "confirmPassword" }
                        control={ form.control }
                        render={ ( { field, fieldState } ) => (
                            <Field data-invalid={ fieldState.invalid }>
                                <FieldLabel htmlFor={ "confirmPassword-field" }>Confirm password</FieldLabel>
                                <Input
                                    { ...field }
                                    id={ "confirmPassword-field" }
                                    aria-invalid={ fieldState.invalid }
                                    placeholder="Confirm your new password"
                                    type={ "password" }
                                    autoComplete={ "new-password" }
                                    disabled={ isSubmitting || isPending }
                                />
                                { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                                </FieldError> }
                            </Field>
                        ) }
                    />
                    { !isSuccess && isError && errors.root && (
                        <FieldGroup>
                            <Alert variant="destructive">
                                <AlertCircleIcon/>
                                <AlertTitle>Failed to changed password</AlertTitle>
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
                                    Password changed
                                </AlertTitle>
                            </Alert>
                        </FieldGroup>
                    ) }
                    { !isValid ? null :
                        <Button type="submit" variant="default"
                                disabled={ isSubmitting || isPending || isSuccess }>
                            { isSubmitting || isPending ?
                                <>
                                    { "Processing" }
                                    <Spinner/>
                                </> : "Change Password" }
                        </Button>
                    }
                </FieldGroup>
            </FieldSet>
        </form>
    )
}
