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


import { useRouter } from "@bprogress/next/app";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { EmployeeUpdateSchema } from "@/types/employees/employee.types";
import { useUpdateEmployee } from "@/api/rest/generated/query/employees/employees";
import {
    AddressFragment,
    BankAccountFragment,
    IdentityFragment,
    InsuranceFragment,
} from "@/components/employees/form/fragments";
import { EmployeeDTO } from "@/api/rest/generated/fetch/openAPIDefinition.schemas";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { getUpdateEmployeeInvalidations } from "@/components/employees/form/invalidations";
import { FormBuilder } from "@/components/custom-ui/form/form";

type EmployeeEditFormProps = {
    employee: EmployeeDTO
}

export default function EmployeeEditForm( { employee }: Readonly<EmployeeEditFormProps> ) {
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

    const { isPending, isSuccess, isError, mutate } = useUpdateEmployee();

    const submitHandler = useFormHandler( {
        mutate,
        form,
        variables: ( data ) => ( {
            id: employee.id,
            data: {
                ...data,
                dateOfBirth: data.dateOfBirth?.toLocaleDateString( "en-CA" ) ?? undefined
            }
        } ),
        setInvalidations: ( data ) => getUpdateEmployeeInvalidations( data ),
        onSuccessCallbackAction: () => router.back(),
        setToastSuccessOptions: ( data ) => ( {
            description: "Employee " + data.fullname + " was updated",
            action: {
                label: "View Employee",
                onClick: () => router.push( "/app/manage/employees/" + data.id )
            }
        } )
    } );

    return <FormBuilder form={ form } formId={ "employee-edit-form" }
                        status={ { isPending, isError, isSuccess } }
                        submitHandler={ submitHandler }
                        cancelHandler={ () => router.back() }
                        options={ {
                            actionLabel: "Save",
                            successMessage: "Employee was updated successfully",
                            maxScrollHeightClassName: "h-[65vh]"
                        } }
    >
        <IdentityFragment/>
        <AddressFragment/>
        <BankAccountFragment/>
        <InsuranceFragment/>
    </FormBuilder>
}
