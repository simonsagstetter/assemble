/*
 * assemble
 * EmployeeCreateForm.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";

import { useQueryClient } from "@tanstack/react-query";
import { useRouter } from "@bprogress/next/app";
import { FormProvider, useForm } from "react-hook-form";
import { EmployeeCreateFormData, EmployeeCreateSchema } from "@/types/employees/employee.types";
import { zodResolver } from "@hookform/resolvers/zod";
import { getGetAllEmployeesQueryKey, useCreateEmployee } from "@/api/rest/generated/query/employees/employees";
import { FormActionContext } from "@/store/formActionStore";
import { Separator } from "@/components/ui/separator";
import { FormActions } from "@/components/custom-ui/form/actions";
import { FieldGroup } from "@/components/ui/field";
import { ErrorMessage, SuccessMessage } from "@/components/custom-ui/form/messages";
import { FieldValidationError } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { toast } from "sonner";
import { ScrollArea } from "@/components/ui/scroll-area";
import {
    AddressFragment,
    BankAccountFragment,
    IdentityFragment, InsuranceFragment,
    UserLookupFragment
} from "@/components/manage/employees/form/fragments";
import useModalContext from "@/hooks/useModalContext";

export default function EmployeeCreateForm() {
    const modalContext = useModalContext();
    const queryClient = useQueryClient();
    const router = useRouter();

    const form = useForm( {
        resolver: zodResolver( EmployeeCreateSchema ),
        defaultValues: {
            firstname: "",
            lastname: "",
            email: "",
            phone: "",
            placeOfBirth: "",
            dateOfBirth: undefined,
            citizenship: "",
            maritalStatus: "SINGLE",
            userId: "",
            taxIdentificationNumber: "",
            healthInsurance: "",
            nationalInsuranceNumber: "",
            address: {
                street: "",
                number: "",
                city: "",
                state: "",
                postalCode: "",
                country: ""
            },
            bankAccount: {
                holderName: "",
                institutionName: "",
                iban: "",
                bic: ""
            }
        }
    } )

    const createEmployee = useCreateEmployee();

    const { isPending, isSuccess, isError, mutate } = createEmployee;

    const handleCreateEmployee = async ( data: EmployeeCreateFormData ) => {
        mutate(
            {
                data: {
                    ...data,
                    dateOfBirth: data.dateOfBirth?.toISOString() ?? undefined
                }
            },
            {
                onSuccess: async ( employee ) => {
                    await queryClient.invalidateQueries( {
                        queryKey: getGetAllEmployeesQueryKey()
                    } )
                    form.clearErrors();
                    toast.success( "Success", {
                        description: "Employee " + employee.fullname + " was created",
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
                        if ( "errors" in data && data.errors ) {
                            data.errors.forEach( ( error: FieldValidationError ) => {
                                form.setError( error.fieldName as keyof EmployeeCreateFormData, {
                                    type: "manual",
                                    message: error.errorMessage
                                } )
                            } )
                        } else if ( "message" in data && data.message ) {
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
        value={ { isPending, isSuccess, isError, handleCancel, disableOnSuccess: true } }>
        <FormProvider { ...form }>
            <form id={ "employee-create-form" }
                  onSubmit={ form.handleSubmit( handleCreateEmployee ) }
                  className={ "space-y-8" }
            >
                <ScrollArea className={ `${ modalContext ? "h-[65vh] my-0" : "" }` }>
                    <FieldGroup className={ "py-4 px-8" }>
                        <IdentityFragment/>
                        <UserLookupFragment/>
                        <AddressFragment/>
                        <BankAccountFragment/>
                        <InsuranceFragment/>
                        <ErrorMessage/>
                        <SuccessMessage message={ "Employee was created successfully" }/>
                    </FieldGroup>
                </ScrollArea>
                <Separator className={ "my-0" }/>
                <FormActions formId={ "employee-create-form" } label={ "New" }/>
            </form>
        </FormProvider>
    </FormActionContext.Provider>
}