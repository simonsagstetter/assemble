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

import { Employee } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import useModalContext from "@/hooks/useModalContext";
import { useRouter } from "@bprogress/next/app";
import { FormProvider, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
    EmployeeUpdateUserFormData,
    EmployeeUpdateUserSchema
} from "@/types/employees/employee.types";
import {
    getGetAllEmployeesQueryKey,
    getGetEmployeeQueryKey,
    useUpdateEmployeeUser
} from "@/api/rest/generated/query/employees/employees";
import { FormActionContext } from "@/store/formActionStore";
import { ScrollArea } from "@/components/ui/scroll-area";
import { FieldGroup } from "@/components/ui/field";
import {
    UserLookupFragment
} from "@/components/manage/employees/form/fragments";
import { ErrorMessage, SuccessMessage } from "@/components/custom-ui/form/messages";
import { Separator } from "@/components/ui/separator";
import { FormActions } from "@/components/custom-ui/form/actions";
import { toast } from "sonner";
import { useQueryClient } from "@tanstack/react-query";

type EmployeeUpdateUserFormProps = {
    employee: Employee
}

export default function EmployeeUpdateUserForm( { employee }: EmployeeUpdateUserFormProps ) {
    const modalContext = useModalContext();
    const queryClient = useQueryClient();
    const router = useRouter();

    const form = useForm( {
        resolver: zodResolver( EmployeeUpdateUserSchema ),
        defaultValues: {
            userId: ""
        }
    } );

    const { mutate, isError, isSuccess, isPending } = useUpdateEmployeeUser();

    const handleUpdateEmployeeUser = ( data: EmployeeUpdateUserFormData ) => {
        mutate(
            {
                id: employee.id,
                data: {
                    userId: data.userId
                }
            },
            {
                onSuccess: async () => {
                    await queryClient.invalidateQueries( {
                        queryKey: getGetAllEmployeesQueryKey()
                    } );
                    await queryClient.invalidateQueries( {
                        queryKey: getGetEmployeeQueryKey( employee.id )
                    } );
                    form.clearErrors();
                    toast.success( "Success", {
                        description: "Employee " + employee.fullname + " was updated",
                        action: {
                            label: "View Employee",
                            onClick: () => router.push( "/app/manage/employees/" + employee.id )
                        }
                    } );
                    if ( modalContext ) {
                        handleCancel();
                    }
                },
                onError: ( error ) => {
                    if ( ( error.status === 400 || error.status === 404 ) && error.response?.data ) {
                        const data = error.response.data;
                        if ( "message" in data && data.message ) {
                            form.setError( "root", { type: "manual", message: data.message } )
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
            router.back();
        } else {
            if ( !isSuccess ) router.back();
            else router.push( "/app/manage/employees" );
        }
    }

    return <FormActionContext.Provider
        value={ { isPending, isSuccess, isError, handleCancel, disableOnSuccess: false } }>
        <FormProvider { ...form }>
            <form id={ "employee-update-user-form" }
                  onSubmit={ form.handleSubmit( handleUpdateEmployeeUser ) }
                  className={ "space-y-8" }
            >
                <ScrollArea className={ `${ modalContext ? "h-auto my-0" : "" }` }>
                    <FieldGroup className={ "py-4 px-8" }>
                        <UserLookupFragment/>
                        <ErrorMessage/>
                        <SuccessMessage message={ "Employee was updated successfully" }/>
                    </FieldGroup>
                </ScrollArea>
                <Separator className={ "my-0" }/>
                <FormActions formId={ "employee-update-user-form" } label={ "Save" }/>
            </form>
        </FormProvider>
    </FormActionContext.Provider>

}