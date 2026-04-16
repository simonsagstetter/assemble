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

import { UserAdmin } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { useRouter } from "@bprogress/next/app";
import { useForm } from "react-hook-form";
import { useUpdateUserRoles, } from "@/api/rest/generated/query/user-management/user-management";
import { UserUpdateRolesFormData, UserUpdateRolesSchema } from "@/types/users/user.types";
import { zodResolver } from "@hookform/resolvers/zod";
import { FieldGroup, FieldSet } from "@/components/ui/field";
import { RolesLookupField } from "@/components/users/form/custom-fields";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { getUpdateUserRoleInvalidations } from "@/components/users/form/invalidations";
import { FormBuilder } from "@/components/custom-ui/form/form";

type UserUpdateRolesFormProps = {
    user: UserAdmin
}

export default function UserUpdateRolesForm( { user }: Readonly<UserUpdateRolesFormProps> ) {
    const router = useRouter();
    const { roles } = user;
    const form = useForm<UserUpdateRolesFormData>( {
        resolver: zodResolver( UserUpdateRolesSchema ),
        defaultValues: {
            roles: [ ...roles ]
        }
    } );
    const { isSubmitting } = form.formState;
    const { mutate, isPending, isSuccess, isError } = useUpdateUserRoles();

    const submitHandler = useFormHandler( {
        mutate,
        form,
        variables: ( data ) => ( {
            id: user.id,
            data
        } ),
        setInvalidations: ( data ) => getUpdateUserRoleInvalidations( data ),
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
                        formId={ "edit-user-role-form" }
                        status={ { isPending, isError, isSuccess } }
                        submitHandler={ submitHandler }
                        cancelHandler={ () => router.back() }
                        options={ {
                            actionLabel: "Save",
                            successMessage: "User was updated successfully",
                        } }
    >
        <FieldSet>
            <FieldGroup>
                <RolesLookupField fieldName={ "roles" } formControl={ form.control }
                                  disabled={ isPending || isSubmitting || isSuccess }/>
            </FieldGroup>
        </FieldSet>
    </FormBuilder>
}
