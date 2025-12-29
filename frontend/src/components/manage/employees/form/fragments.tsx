/*
 * assemble
 * fragments.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { FieldDescription, FieldGroup, FieldLegend, FieldSet } from "@/components/ui/field";
import { CalendarField, InputField, SelectField } from "@/components/custom-ui/form/fields";
import { EmployeeMaritalStatus } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { useFormContext } from "react-hook-form";
import useFormActionContext from "@/hooks/useFormActionContext";
import { UserLookupField } from "@/components/manage/employees/form/custom-fields";

function useCurrentContext() {
    const form = useFormContext();
    const { isPending, isSuccess, disableOnSuccess } = useFormActionContext();
    const { isSubmitting } = form.formState;
    const disabled = disableOnSuccess ? isPending || isSubmitting || isSuccess : isPending || isSubmitting;

    return { form, disabled };
}

function IdentityFragment() {
    const { form, disabled } = useCurrentContext();
    return <FieldSet>
        <FieldLegend>Identity</FieldLegend>
        <FieldDescription>Required information about the user</FieldDescription>
        <FieldGroup>
            <div className={ "grid grid-cols-2 gap-16" }>
                <InputField fieldName={ "firstname" }
                            formControl={ form.control }
                            label={ "Firstname" }
                            placeholder={ "e.g. Max" }
                            disabled={ disabled }
                            autoFocus
                >
                    This field is required
                </InputField>
                <InputField fieldName={ "lastname" }
                            formControl={ form.control }
                            label={ "Lastname" }
                            placeholder={ "e.g. Musterperson" }
                            disabled={ disabled }
                >
                    This field is required
                </InputField>
            </div>
            <div className={ "grid grid-cols-2 gap-16" }>
                <InputField fieldName={ "email" }
                            formControl={ form.control }
                            label={ "E-Mail" }
                            placeholder={ "e.g. max.musterperson@example.com" }
                            type={ "email" }
                            disabled={ disabled }
                />
                <InputField fieldName={ "phone" }
                            formControl={ form.control }
                            label={ "Phone" }
                            type={ "tel" }
                            placeholder={ "e.g. +4912345678" }
                            disabled={ disabled }
                />
            </div>
            <div className={ "grid grid-cols-2 gap-16" }>
                <InputField fieldName={ "placeOfBirth" }
                            formControl={ form.control }
                            label={ "Place of Birth" }
                            placeholder={ "e.g. Berlin" }
                            disabled={ disabled }
                />
                <CalendarField fieldName={ "dateOfBirth" }
                               formControl={ form.control }
                               placeholder={ "Select birth date" }
                               label={ "Date of Birth" }
                               disabled={ disabled }
                />
            </div>
            <div className={ "grid grid-cols-2 gap-16" }>
                <InputField fieldName={ "citizenship" }
                            formControl={ form.control }
                            label={ "Citizenship" }
                            placeholder={ "e.g. german" }
                            disabled={ disabled }
                />
                <SelectField fieldName={ "maritalStatus" }
                             formControl={ form.control }
                             label={ "Marital Status" }
                             placeholder={ "Choose a marital status" }
                             options={ [
                                 { label: "Single", value: EmployeeMaritalStatus.SINGLE },
                                 { label: "Civil Partnership", value: EmployeeMaritalStatus.CIVIL },
                                 { label: "Married", value: EmployeeMaritalStatus.MARRIED },
                                 { label: "Divorced", value: EmployeeMaritalStatus.DIVORCED },
                                 { label: "Widowed", value: EmployeeMaritalStatus.WIDOWED },
                             ] }
                             disabled={ disabled }
                >

                </SelectField>
            </div>
        </FieldGroup>
    </FieldSet>
}

function UserLookupFragment() {
    const { form, disabled } = useCurrentContext();
    return <FieldSet>
        <FieldLegend>Resources</FieldLegend>
        <FieldDescription>Connect an employee to other resources</FieldDescription>
        <FieldGroup>
            <UserLookupField fieldName={ "userId" } formControl={ form.control }
                             disabled={ disabled }/>
        </FieldGroup>
    </FieldSet>
}

function AddressFragment() {
    const { form, disabled } = useCurrentContext();
    return <FieldSet>
        <FieldLegend>Address</FieldLegend>
        <FieldDescription>This section is optional</FieldDescription>
        <FieldGroup>
            <div className={ "grid grid-cols-2 gap-16" }>
                <InputField fieldName={ "address.street" }
                            formControl={ form.control }
                            label={ "Street" }
                            placeholder={ "e.g. Berliner Straße" }
                            disabled={ disabled }
                />
                <InputField fieldName={ "address.number" }
                            formControl={ form.control }
                            label={ "Number" }
                            placeholder={ "e.g. 100" }
                            disabled={ disabled }
                />
            </div>
            <div className={ "grid grid-cols-2 gap-16" }>
                <InputField fieldName={ "address.postalCode" }
                            formControl={ form.control }
                            label={ "Postal Code" }
                            placeholder={ "e.g. 12345" }
                            disabled={ disabled }
                />
                <InputField fieldName={ "address.city" }
                            formControl={ form.control }
                            label={ "City" }
                            placeholder={ "e.g. Berlin" }
                            disabled={ disabled }
                />
            </div>
            <div className={ "grid grid-cols-2 gap-16" }>
                <InputField fieldName={ "address.state" }
                            formControl={ form.control }
                            label={ "State" }
                            placeholder={ "e.g. Berlin" }
                            disabled={ disabled }
                />
                <InputField fieldName={ "address.country" }
                            formControl={ form.control }
                            label={ "Country" }
                            placeholder={ "e.g. Germany" }
                            disabled={ disabled }
                />
            </div>
        </FieldGroup>
    </FieldSet>
}

function BankAccountFragment() {
    const { form, disabled } = useCurrentContext();
    return <FieldSet>
        <FieldLegend>Bank Account</FieldLegend>
        <FieldDescription>This section is optional</FieldDescription>
        <FieldGroup>
            <div className={ "grid grid-cols-2 gap-16" }>
                <InputField fieldName={ "bankAccount.holderName" }
                            formControl={ form.control }
                            label={ "Holder" }
                            placeholder={ "e.g. Max Musterperson" }
                            disabled={ disabled }
                />
                <InputField fieldName={ "bankAccount.institutionName" }
                            formControl={ form.control }
                            label={ "Institution Name" }
                            placeholder={ "e.g. DKB Bank" }
                            disabled={ disabled }
                />
            </div>
            <div className={ "grid grid-cols-2 gap-16" }>
                <InputField fieldName={ "bankAccount.iban" }
                            formControl={ form.control }
                            label={ "IBAN" }
                            placeholder={ "e.g. DE89370400440532013000" }
                            disabled={ disabled }
                />
                <InputField fieldName={ "bankAccount.bic" }
                            formControl={ form.control }
                            label={ "BIC" }
                            placeholder={ "e.g. BDYLAEXXX" }
                            disabled={ disabled }
                />
            </div>
            <InputField fieldName={ "taxIdentificationNumber" }
                        formControl={ form.control }
                        label={ "Tax Identification Number" }
                        placeholder={ "e.g. 1237862138" }
                        disabled={ disabled }
            />
        </FieldGroup>
    </FieldSet>
}

function InsuranceFragment() {
    const { form, disabled } = useCurrentContext();
    return <FieldSet>
        <FieldLegend>Insurance</FieldLegend>
        <FieldDescription>This section is optional</FieldDescription>
        <FieldGroup>
            <div className={ "grid grid-cols-2 gap-16" }>
                <InputField fieldName={ "healthInsurance" }
                            formControl={ form.control }
                            label={ "Health Insurance" }
                            placeholder={ "e.g. AOK Nord" }
                            disabled={ disabled }
                />
                <InputField fieldName={ "nationalInsuranceNumber" }
                            formControl={ form.control }
                            label={ "National Insurance Number" }
                            placeholder={ "e.g. SE2139879SDS" }
                            disabled={ disabled }
                />
            </div>
        </FieldGroup>
    </FieldSet>
}

export { IdentityFragment, UserLookupFragment, AddressFragment, BankAccountFragment, InsuranceFragment };