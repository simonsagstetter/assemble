/*
 * assemble
 * EmployeeDeleteForm.tsx
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
import { useDeleteEmployee } from "@/api/rest/generated/query/employees/employees";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { getDeleteEmployeeInvalidations } from "@/components/employees/form/invalidations";
import { FormBuilder } from "@/components/custom-ui/form/form";

type EmployeeDeleteFormProps = {
    employee: EmployeeDTO
}

export default function EmployeeDeleteForm( { employee }: Readonly<EmployeeDeleteFormProps> ) {
    const router = useRouter();
    const form = useForm();
    const deleteEmployee = useDeleteEmployee();
    const { isPending, isError, isSuccess, mutate } = deleteEmployee;

    const submitHandler = useFormHandler( {
        mutate,
        form,
        variables: () => ( { id: employee.id } ),
        setInvalidations: () => getDeleteEmployeeInvalidations( employee ),
        onSuccessCallbackAction: () => router.push( "/app/manage/employees" ),
        setToastSuccessOptions: () => ( {
            description: "Employee deleted"
        } )
    } );


    return <FormBuilder form={ form } formId={ "employee-delete-form" }
                        status={ { isSuccess, isError, isPending } }
                        submitHandler={ submitHandler }
                        cancelHandler={ () => router.back() }
                        options={ {
                            actionLabel: "Delete",
                            actionVariant: "destructive",
                            disabledOnSuccess: true,
                            successMessage: "Employee was deleted successfully."
                        } }
    >
        <p className={ "text-xl font-semibold text-center" }>
            Are you sure you want to delete employee <strong>{ employee.fullname }</strong>?
        </p>
    </FormBuilder>
}
