/*
 * assemble
 * UserUpdateEmployeeForm.tsx
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
import { useUpdateUserEmployee, } from "@/api/rest/generated/query/user-management/user-management";
import { UserUpdateEmpployeeFormData, UserUpdateEmpployeeSchema, } from "@/types/users/user.types";
import { zodResolver } from "@hookform/resolvers/zod";
import { FieldGroup, FieldSet } from "@/components/ui/field";
import { UnlinkedEmployeeLookupField } from "@/components/employees/form/custom-fields";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { getUpdateUserEmployeeInvalidations } from "@/components/users/form/invalidations";
import { FormBuilder } from "@/components/custom-ui/form/form";

type UpdateEmployeeFormProps = {
    user: UserAdmin
}

export default function UserUpdateEmployeeForm( { user }: Readonly<UpdateEmployeeFormProps> ) {
    const router = useRouter();
    const form = useForm<UserUpdateEmpployeeFormData>( {
        resolver: zodResolver( UserUpdateEmpployeeSchema ),
        defaultValues: {
            employeeId: ""
        }
    } );
    const { isSubmitting } = form.formState;
    const { mutate, isPending, isSuccess, isError } = useUpdateUserEmployee();

    const submitHandler = useFormHandler( {
        mutate,
        form,
        variables: ( data ) => ( {
            id: user.id,
            data: {
                employeeId: data.employeeId || undefined
            }
        } ),
        setInvalidations: ( data ) => getUpdateUserEmployeeInvalidations( data ),
        onSuccessCallbackAction: () => router.back(),
        setToastSuccessOptions: ( data ) => ( {
            description: "User " + data.username + " was updated",
            action: {
                label: "View User",
                onClick: () => router.push( "/app/admin/users/" + data.id )
            }
        } )
    } )


    return <FormBuilder form={ form }
                        formId={ "user-edit-employee-form" }
                        status={ { isError, isPending, isSuccess } }
                        submitHandler={ submitHandler }
                        cancelHandler={ () => router.back() }
                        options={ {
                            actionLabel: "Save",
                            successMessage: "User was updated successfully"
                        } }
    >
        <FieldSet>
            <FieldGroup>
                <UnlinkedEmployeeLookupField fieldName={ "employeeId" } formControl={ form.control }
                                             disabled={ isPending || isSubmitting || isSuccess }/>
            </FieldGroup>
        </FieldSet>
    </FormBuilder>
}

