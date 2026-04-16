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

import { useRouter } from "@bprogress/next/app";
import { useForm } from "react-hook-form";
import { EmployeeCreateSchema } from "@/types/employees/employee.types";
import { zodResolver } from "@hookform/resolvers/zod";
import { useCreateEmployee } from "@/api/rest/generated/query/employees/employees";
import {
    AddressFragment,
    BankAccountFragment,
    IdentityFragment,
    InsuranceFragment,
    UserLookupFragment
} from "@/components/employees/form/fragments";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { getCreateEmployeeInvalidations } from "@/components/employees/form/invalidations";
import { FormBuilder } from "@/components/custom-ui/form/form";
import { EmployeeDTOMaritalStatus } from "@/api/rest/generated/query/openAPIDefinition.schemas";

export default function EmployeeCreateForm() {
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
            maritalStatus: EmployeeDTOMaritalStatus.SINGLE,
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

    const { isPending, isSuccess, isError, mutate } = useCreateEmployee();

    const submitHandler = useFormHandler( {
        form,
        mutate,
        variables: ( data ) => ( {
            data: {
                ...data,
                dateOfBirth: data.dateOfBirth?.toLocaleDateString( "en-CA" ) ?? undefined
            }
        } ),
        setInvalidations: () => getCreateEmployeeInvalidations(),
        setToastSuccessOptions: ( data ) => ( {
            description: "Employee " + data.fullname + " was created",
            action: {
                label: "View Employee",
                onClick: () => router.push( "/app/manage/employees/" + data.id )
            }
        } ),
        onSuccessCallbackAction: () => router.back()

    } )

    return <FormBuilder
        form={ form }
        formId={ "employee-create-form" }
        status={ {
            isPending, isSuccess, isError
        } }
        options={ {
            actionLabel: "New",
            disabledOnSuccess: true,
            maxScrollHeightClassName: "h-[65vh]",
            successMessage: "Employee was created successfully"
        } }
        submitHandler={ submitHandler }
        cancelHandler={ () => router.back() }>
        <IdentityFragment/>
        <UserLookupFragment/>
        <AddressFragment/>
        <BankAccountFragment/>
        <InsuranceFragment/>
    </FormBuilder>
}