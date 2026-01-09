/*
 * assemble
 * custom-fields.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { Control, FieldValues, Path } from "react-hook-form";
import { CustomField } from "@/components/custom-ui/form/fields";
import { Field, FieldDescription, FieldError, FieldLabel } from "@/components/ui/field";
import UserSearch from "@/components/manage/employees/form/UserSearch";
import EmployeeSearch from "@/components/manage/employees/form/EmployeeSearch";
import UnlinkedEmployeeSearch from "@/components/manage/employees/form/UnlinkedEmployeeSearch";

function UserLookupField<TFieldValues extends FieldValues, TTransformedValues extends FieldValues>(
    { fieldName, formControl, disabled }
    :
    {
        fieldName: Path<TFieldValues>,
        formControl: Control<TFieldValues, unknown, TTransformedValues>,
        disabled: boolean
    }
) {
    return <CustomField
        fieldName={ fieldName }
        formControl={ formControl }
        renderAction={ ( { field, fieldState } ) => (
            <Field data-invalid={ fieldState.invalid }>
                <FieldLabel htmlFor={ `${ fieldName }-field` }>User</FieldLabel>
                <UserSearch field={ field } disabled={ disabled }/>
                <FieldDescription>
                    Connect a user to this employee. Leave this field empty if you want to
                    assign a user later.
                </FieldDescription>
                { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                </FieldError> }
            </Field>
        ) }/>
}

function EmployeeLookupField<TFieldValues extends FieldValues, TTransformedValues extends FieldValues>(
    { fieldName, formControl, disabled, excludeValues, onSelectHandler }
    :
    {
        fieldName: Path<TFieldValues>,
        formControl: Control<TFieldValues, unknown, TTransformedValues>,
        disabled: boolean,
        excludeValues: string[],
        onSelectHandler?: ( value: string ) => void
    }
) {
    return <CustomField
        fieldName={ fieldName }
        formControl={ formControl }
        renderAction={ ( { field, fieldState } ) => (
            <Field data-invalid={ fieldState.invalid }>
                <FieldLabel htmlFor={ `${ fieldName }-field` }>Employee</FieldLabel>
                <EmployeeSearch field={ field }
                                disabled={ disabled }
                                excludeValues={ excludeValues }
                                onSelectAction={ onSelectHandler }
                />
                { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                </FieldError> }
            </Field>
        ) }/>
}

function UnlinkedEmployeeLookupField<TFieldValues extends FieldValues, TTransformedValues extends FieldValues>(
    { fieldName, formControl, disabled }
    :
    {
        fieldName: Path<TFieldValues>,
        formControl: Control<TFieldValues, unknown, TTransformedValues>,
        disabled: boolean
    }
) {
    return <CustomField
        fieldName={ fieldName }
        formControl={ formControl }
        renderAction={ ( { field, fieldState } ) => (
            <Field data-invalid={ fieldState.invalid }>
                <FieldLabel htmlFor={ `${ fieldName }-field` }>Employee</FieldLabel>
                <UnlinkedEmployeeSearch field={ field }
                                        disabled={ disabled }/>
                <FieldDescription>
                    Connect an employee to user. Leave this field empty if you want to
                    assign an employee later.
                </FieldDescription>
                { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                </FieldError> }
            </Field>
        ) }/>
}

export {
    UserLookupField,
    EmployeeLookupField,
    UnlinkedEmployeeLookupField
}