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
import { useQueryClient } from "@tanstack/react-query";
import { FormProvider, useForm } from "react-hook-form";
import {
    getGetAllUsersQueryKey,
    getGetUserByIdQueryKey,
    useUpdateUserEmployee,
} from "@/api/rest/generated/query/user-management/user-management";
import { UserUpdateEmpployeeFormData, UserUpdateEmpployeeSchema, } from "@/types/users/user.types";
import { zodResolver } from "@hookform/resolvers/zod";
import { ScrollArea } from "@radix-ui/react-scroll-area";
import { FieldGroup, FieldSet } from "@/components/ui/field";
import { Separator } from "@/components/ui/separator";
import { toast } from "sonner";
import { EmployeeLookupField } from "@/components/admin/users/form/custom-fields";
import useModalContext from "@/hooks/useModalContext";
import { FormActionContext } from "@/store/formActionStore";
import { ErrorMessage, SuccessMessage } from "@/components/custom-ui/form/messages";
import { FormActions } from "@/components/custom-ui/form/actions";

type UpdateEmployeeFormProps = {
    user: UserAdmin
}

export default function UserUpdateEmployeeForm( { user }: UpdateEmployeeFormProps ) {
    const modalContext = useModalContext();
    const router = useRouter();
    const queryClient = useQueryClient();
    const form = useForm<UserUpdateEmpployeeFormData>( {
        resolver: zodResolver( UserUpdateEmpployeeSchema ),
        defaultValues: {
            employeeId: ""
        }
    } );
    const { errors, isSubmitting } = form.formState;
    const { mutate, isPending, isSuccess, isError } = useUpdateUserEmployee();

    const handleUpdateEmployee = ( data: UserUpdateEmpployeeFormData ) => {
        mutate(
            {
                id: user.id,
                data: {
                    employeeId: data.employeeId ? data.employeeId : undefined
                }
            },
            {
                onSuccess: async ( user ) => {
                    form.clearErrors();
                    await queryClient.invalidateQueries( {
                        queryKey: getGetAllUsersQueryKey(),
                        refetchType: "all"
                    } )
                    await queryClient.invalidateQueries( {
                        queryKey: getGetUserByIdQueryKey( user.id ),
                        refetchType: "all"
                    } )
                    toast.success( "Success", {
                        description: "User " + user.username + " was updated",
                        action: {
                            label: "View User",
                            onClick: () => router.push( "/app/admin/users/" + user.id )
                        }
                    } );
                    if ( modalContext ) {
                        handleCancel();
                    }
                },
                onError: ( error ) => {
                    if ( error.response?.data ) {
                        const data = error.response.data;
                        if ( ( error.status === 404 || error.status === 400 ) && "message" in data && data.message ) {
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
            else router.replace( "/app/admin/users" );
        }
    }

    return <FormActionContext.Provider
        value={ { isPending, isError, isSuccess, handleCancel, disableOnSuccess: false } }>
        <FormProvider { ...form }>
            <form id={ "user-update-employee-form" } onSubmit={ form.handleSubmit( handleUpdateEmployee ) }
                  className="space-y-8">
                <ScrollArea className={ `${ modalContext ? "my-0" : "" }` }>
                    <FieldGroup className={ "py-4 px-8" }>
                        <FieldSet>
                            <FieldGroup>
                                <EmployeeLookupField fieldName={ "employeeId" } formControl={ form.control }
                                                     disabled={ isPending || isSubmitting || isSuccess }/>
                            </FieldGroup>
                        </FieldSet>
                        <ErrorMessage/>
                        <SuccessMessage message={ "User was updated successfully" }/>
                    </FieldGroup>
                </ScrollArea>
                <Separator className={ "my-0" }/>
                <FormActions formId={ "user-update-employee-form" } label={ "Save" }/>
            </form>
        </FormProvider>
    </FormActionContext.Provider>
}

