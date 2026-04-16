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

import { UserAdmin } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { useRouter } from "@bprogress/next/app";
import { useForm } from "react-hook-form";
import { useUpdateUserPassword } from "@/api/rest/generated/query/user-management/user-management";
import { UserResetPasswordFormData, UserResetPasswordSchema } from "@/types/users/user.types";
import { zodResolver } from "@hookform/resolvers/zod";
import { FieldGroup, FieldSet } from "@/components/ui/field";
import { InputField, SwitchField } from "@/components/custom-ui/form/fields";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { getResetUserPasswordInvalidations } from "@/components/users/form/invalidations";
import { FormBuilder } from "@/components/custom-ui/form/form";

export type UserResetPasswordFormProps = {
    user: UserAdmin
}

export default function UserResetPasswordForm( { user }: Readonly<UserResetPasswordFormProps> ) {
    const router = useRouter();
    const form = useForm<UserResetPasswordFormData>( {
        resolver: zodResolver( UserResetPasswordSchema ),
        defaultValues: {
            newPassword: "",
            invalidateAllSessions: false
        }
    } );
    const { isSubmitting } = form.formState;

    const { mutate, isPending, isSuccess, isError } = useUpdateUserPassword();

    const submitHandler = useFormHandler( {
        mutate,
        form,
        variables: ( data ) => ( {
            id: user.id,
            data
        } ),
        setInvalidations: () => getResetUserPasswordInvalidations( user ),
        onSuccessCallbackAction: () => {
            if ( form.getValues( "invalidateAllSessions" ) ) router.refresh();
            router.back();
        },
        setToastSuccessOptions: () => ( {
            description: "Password was reset"
        } )
    } )

    return <FormBuilder form={ form }
                        formId={ "reset-user-password-form" }
                        status={ {
                            isPending,
                            isError,
                            isSuccess
                        } }
                        submitHandler={ submitHandler }
                        cancelHandler={ () => router.back() }
                        options={ {
                            actionLabel: "Save",
                            successMessage: "Password was reset successfully"
                        } }
    >
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
    </FormBuilder>
}
