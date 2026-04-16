/*
 * assemble
 * EmployeeUpdateUserForm.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";

import { EmployeeDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { useRouter } from "@bprogress/next/app";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { EmployeeUpdateUserSchema } from "@/types/employees/employee.types";
import { useUpdateEmployeeUser } from "@/api/rest/generated/query/employees/employees";
import { UserLookupFragment } from "@/components/employees/form/fragments";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { getUpdateEmployeeUserInvalidations } from "@/components/employees/form/invalidations";
import { FormBuilder } from "@/components/custom-ui/form/form";

type EmployeeUpdateUserFormProps = {
    employee: EmployeeDTO
}

export default function EmployeeUpdateUserForm( { employee }: Readonly<EmployeeUpdateUserFormProps> ) {
    const router = useRouter();

    const form = useForm( {
        resolver: zodResolver( EmployeeUpdateUserSchema ),
        defaultValues: {
            userId: ""
        }
    } );

    const { mutate, isError, isSuccess, isPending } = useUpdateEmployeeUser();

    const submitHandler = useFormHandler( {
        mutate,
        form,
        variables: ( data ) => ( {
            id: employee.id,
            data
        } ),
        setInvalidations: ( data ) => getUpdateEmployeeUserInvalidations( data ),
        onSuccessCallbackAction: () => router.back(),
        setToastSuccessOptions: ( data ) => ( {
            description: "Employee " + data.fullname + " was updated",
            action: {
                label: "View Employee",
                onClick: () => router.push( "/app/manage/employees/" + data.id )
            }
        } )
    } );

    return <FormBuilder form={ form } formId={ "edit-employee-user-form" }
                        status={ { isError, isSuccess, isPending } }
                        submitHandler={ submitHandler }
                        cancelHandler={ () => router.back() }
                        options={ {
                            actionLabel: "Save",
                            successMessage: "Employee was updated successfully",
                        } }
    >
        <UserLookupFragment/>
    </FormBuilder>;

}