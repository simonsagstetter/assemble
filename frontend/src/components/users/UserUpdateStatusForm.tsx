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
import { useForm } from "react-hook-form";
import { useUpdateUserStatus } from "@/api/rest/generated/query/user-management/user-management";
import { type UserStatusFormData, UserStatusSchema } from "@/types/users/user.types";
import { zodResolver } from "@hookform/resolvers/zod";
import { FieldGroup, FieldSet } from "@/components/ui/field";
import { SwitchField } from "@/components/custom-ui/form/fields";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { getUpdateUserStatusInvalidations } from "@/components/users/form/invalidations";
import { FormBuilder } from "@/components/custom-ui/form/form";

type UserStatusFormProps = {
    user: UserAdmin
}

export default function UserUpdateStatusForm( { user }: Readonly<UserStatusFormProps> ) {
    const router = useRouter();
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

    const submitHandler = useFormHandler( {
        mutate,
        form,
        variables: ( data ) => ( {
            id: user.id,
            data
        } ),
        setInvalidations: ( data ) => getUpdateUserStatusInvalidations( data ),
        onSuccessCallbackAction: () => router.back(),
        setToastSuccessOptions: ( data ) => ( {
            description: "User " + data.username + " was updated",
            action: {
                label: "View User",
                onClick: () => router.push( "/app/admin/users/" + data.id )
            }
        } )
    } );

    return <FormBuilder form={ form }
                        formId={ "edit-user-status-form" }
                        status={ { isPending, isSuccess, isError } }
                        submitHandler={ submitHandler }
                        cancelHandler={ () => router.back() }
                        options={ {
                            actionLabel: "Save",
                            successMessage: "User was updated successfully",
                        } }
    >
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
    </FormBuilder>

}
