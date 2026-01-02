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
import useModalContext from "@/hooks/useModalContext";
import { useQueryClient } from "@tanstack/react-query";
import { useRouter } from "@bprogress/next/app";
import { FormProvider, useForm } from "react-hook-form";
import { toast } from "sonner";
import { FormActionContext } from "@/store/formActionStore";
import { FieldGroup } from "@/components/ui/field";
import { ErrorMessage } from "@/components/custom-ui/form/messages";
import { Separator } from "@/components/ui/separator";
import { FormActions } from "@/components/custom-ui/form/actions";
import {
    getGetAllEmployeesQueryKey,
    getGetEmployeeQueryKey,
    useDeleteEmployee
} from "@/api/rest/generated/query/employees/employees";
import {
    getGetAllProjectAssignmentsByEmployeeIdQueryKey
} from "@/api/rest/generated/query/project-assignments/project-assignments";

type EmployeeDeleteFormProps = {
    employee: EmployeeDTO
}

export default function EmployeeDeleteForm( { employee }: EmployeeDeleteFormProps ) {
    const modalContext = useModalContext();
    const queryClient = useQueryClient();
    const router = useRouter();
    const form = useForm();
    const deleteEmployee = useDeleteEmployee();
    const { isPending, isError, isSuccess, mutate } = deleteEmployee;

    const handleDeleteUser = () => {
        mutate(
            {
                id: employee.id
            },
            {
                onSuccess: async () => {
                    toast.success( "Success", {
                        description: "Employee was deleted",
                    } )
                    await queryClient.invalidateQueries( {
                        queryKey: getGetAllEmployeesQueryKey(),
                        refetchType: "all"
                    } );
                    
                    await queryClient.invalidateQueries( {
                        queryKey: getGetEmployeeQueryKey( employee.id ),
                        refetchType: "none"
                    } );

                    await queryClient.invalidateQueries( {
                        queryKey: getGetAllProjectAssignmentsByEmployeeIdQueryKey( employee.id ),
                        refetchType: "all"
                    } );

                    if ( modalContext ) {
                        modalContext.setOpen( false );
                    }
                    setTimeout( () => router.push( "/app/manage/employees" ), 200 );
                    router.back();
                },
                onError: ( error ) => {
                    if ( error.response?.data ) {
                        const data = error.response.data;
                        if ( "message" in data && data.message ) {
                            form.setError( "root", { type: "manual", message: "Employee could not be deleted." } )
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
        }
        router.back();
    }

    return <FormActionContext.Provider
        value={ { isSuccess, isPending, isError, handleCancel, disableOnSuccess: true } }>
        <FormProvider { ...form }>
            <form id={ "employee-delete-form" } onSubmit={ form.handleSubmit( handleDeleteUser ) }
                  className="space-y-8">
                <FieldGroup className={ "p-8 my-0" }>
                    <p className={ "text-xl font-semibold text-center" }>
                        Are you sure you want to delete employee <strong>{ employee.fullname }</strong>?
                    </p>
                    <ErrorMessage/>
                </FieldGroup>
                <Separator className={ "my-0" }/>
                <FormActions formId={ "employee-delete-form" } label={ "Delete" } variant={ "destructive" }/>
            </form>
        </FormProvider>
    </FormActionContext.Provider>
}
