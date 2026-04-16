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

import { FieldDescription, FieldGroup, FieldLegend, FieldSet } from "@/components/ui/field";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { ChangePasswordFormData, ChangePasswordSchema } from "@/types/auth/auth.types";
import { useChangePassword } from "@/api/rest/generated/query/users/users";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { FormBuilder } from "@/components/custom-ui/form/form";
import { InputField } from "@/components/custom-ui/form/fields";

export default function ChangePasswordForm( { username }: Readonly<{ username: string }> ) {
    const form = useForm( {
        resolver: zodResolver( ChangePasswordSchema ),
        defaultValues: {
            oldPassword: "",
            newPassword: "",
            confirmPassword: ""
        }
    } );

    const { isSubmitting } = form.formState;

    const changePassword = useChangePassword();

    const { isPending, isError, isSuccess, mutate } = changePassword;

    const handleSubmit = useFormHandler( {
        form,
        mutate,
        variables: ( data ) => ( {
            data: {
                oldPassword: data.oldPassword,
                newPassword: data.newPassword
            }
        } ),
        setInvalidations: () => [],
        onSuccessCallbackAction: () => {
            form.reset( { oldPassword: "", newPassword: "", confirmPassword: "" } );
        },
        setToastSuccessOptions: () => ( {
            description: "Password was changed"
        } )
    } );

    const beforeSubmitHandler = ( data: ChangePasswordFormData ) => {
        const { newPassword, confirmPassword } = data;
        if ( newPassword !== confirmPassword ) {
            form.setError( "confirmPassword", { message: "Passwords do not match", type: "manual" } );
            return;
        }
        return handleSubmit( data );
    }


    return <FormBuilder form={ form }
                        formId={ "change-password-form" }
                        status={ {
                            isPending,
                            isError,
                            isSuccess
                        } }
                        options={ {
                            actionLabel: "Change",
                            actionHideCancel: true,
                            actionClassName: "px-0 pt-8",
                            disabledOnSuccess: false,
                            maxScrollHeightClassName: "p-0",
                            successMessage: "Employee was created successfully",
                            formGroupSpacing: "p-0",
                        } }
                        submitHandler={ beforeSubmitHandler }
                        cancelHandler={ () => null }
    >
        <FieldSet>
            <FieldLegend>Change Password</FieldLegend>
            <FieldDescription>Change your password for this application.</FieldDescription>
            <FieldGroup>
                <input type={ "text" } className={ "none hidden" } readOnly={ true } value={ username }
                       id={ "username" } name={ "username" }
                       autoComplete={ "username" }/>
                <InputField formControl={ form.control }
                            fieldName={ "oldPassword" }
                            label={ "Old Password" }
                            placeholder={ "Type in your current password" }
                            type={ "password" }
                            disabled={ isSubmitting || isPending }
                            autoComplete={ "current-password" }
                />
                <InputField formControl={ form.control }
                            fieldName={ "newPassword" }
                            label={ "New Password" }
                            placeholder={ "Type in your new password" }
                            type={ "password" }
                            autoComplete={ "new-password" }
                            disabled={ isSubmitting || isPending }
                />
                <InputField formControl={ form.control }
                            fieldName={ "confirmPassword" }
                            label={ "Confirm Password" }
                            placeholder={ "Confirm your new password" }
                            type={ "password" }
                            autoComplete={ "new-password" }
                            disabled={ isSubmitting || isPending }
                />
            </FieldGroup>
        </FieldSet>
    </FormBuilder>
}
