/*
 * assemble
 * EmployeeEditForm.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";


import useModalContext from "@/hooks/useModalContext";
import { useQueryClient } from "@tanstack/react-query";
import { useRouter } from "@bprogress/next/app";
import { FormProvider, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
    EmployeeUpdateFormData,
    EmployeeUpdateSchema
} from "@/types/employees/employee.types";
import {
    getGetAllEmployeesQueryKey, getGetEmployeeQueryKey,
    useUpdateEmployee
} from "@/api/rest/generated/query/employees/employees";
import { toast } from "sonner";
import { FieldValidationError } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { FormActionContext } from "@/store/formActionStore";
import { ScrollArea } from "@/components/ui/scroll-area";
import { FieldGroup } from "@/components/ui/field";
import {
    AddressFragment,
    BankAccountFragment,
    IdentityFragment, InsuranceFragment,
} from "@/components/manage/employees/form/fragments";
import { ErrorMessage, SuccessMessage } from "@/components/custom-ui/form/messages";
import { Separator } from "@/components/ui/separator";
import { FormActions } from "@/components/custom-ui/form/actions";
import { EmployeeDTO } from "@/api/rest/generated/fetch/openAPIDefinition.schemas";

type EmployeeEditFormProps = {
    employee: EmployeeDTO
}

export default function EmployeeEditForm( { employee }: EmployeeEditFormProps ) {
    const modalContext = useModalContext();
    const queryClient = useQueryClient();
    const router = useRouter();

    const form = useForm( {
        resolver: zodResolver( EmployeeUpdateSchema ),
        defaultValues: {
            firstname: employee.firstname ?? "",
            lastname: employee.lastname ?? "",
            email: employee.email ?? "",
            phone: employee.phone ?? "",
            placeOfBirth: employee.placeOfBirth ?? "",
            dateOfBirth: employee.dateOfBirth ? new Date( employee.dateOfBirth ) : undefined,
            citizenship: employee.citizenship ?? "",
            maritalStatus: employee.maritalStatus ?? "",
            taxIdentificationNumber: employee.taxIdentificationNumber ?? "",
            healthInsurance: employee.healthInsurance ?? "",
            nationalInsuranceNumber: employee.nationalInsuranceNumber ?? "",
            address: {
                street: employee.address?.street ?? "",
                number: employee.address?.number ?? "",
                city: employee.address?.city ?? "",
                state: employee.address?.state ?? "",
                postalCode: employee.address?.postalCode ?? "",
                country: employee.address?.country ?? ""
            },
            bankAccount: {
                holderName: employee.bankAccount?.holderName ?? "",
                institutionName: employee.bankAccount?.institutionName ?? "",
                iban: employee.bankAccount?.iban ?? "",
                bic: employee.bankAccount?.bic ?? ""
            }
        }
    } )

    const updateEmployee = useUpdateEmployee();

    const { isPending, isSuccess, isError, mutate } = updateEmployee;

    const handleUpdateEmployee = async ( data: EmployeeUpdateFormData ) => {
        mutate(
            {
                id: employee.id,
                data: {
                    ...data,
                    dateOfBirth: data.dateOfBirth?.toLocaleDateString( "en-CA" ) ?? undefined
                }
            },
            {
                onSuccess: async ( employee ) => {
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
                    handleCancel();
                },
                onError: ( error ) => {
                    if ( ( error.status === 400 || error.status === 404 ) && error.response?.data ) {
                        const data = error.response.data;
                        if ( "errors" in data && data.errors ) {
                            data.errors.forEach( ( error: FieldValidationError ) => {
                                form.setError( error.fieldName as keyof EmployeeUpdateFormData, {
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
        router.back();
    }

    return <FormActionContext.Provider
        value={ { isPending, isSuccess, isError, handleCancel, disableOnSuccess: false } }>
        <FormProvider { ...form }>
            <form id={ "employee-edit-form" }
                  onSubmit={ form.handleSubmit( handleUpdateEmployee ) }
                  className={ "space-y-8" }
            >
                <ScrollArea className={ `${ modalContext ? "h-[65vh] my-0" : "" }` }>
                    <FieldGroup className={ "py-4 px-8" }>
                        <IdentityFragment/>
                        <AddressFragment/>
                        <BankAccountFragment/>
                        <InsuranceFragment/>
                        <ErrorMessage/>
                        <SuccessMessage message={ "Employee was updated successfully" }/>
                    </FieldGroup>
                </ScrollArea>
                <Separator className={ "my-0" }/>
                <FormActions formId={ "employee-edit-form" } label={ "Save" }/>
            </form>
        </FormProvider>
    </FormActionContext.Provider>
}
