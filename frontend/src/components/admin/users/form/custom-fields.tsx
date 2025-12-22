/*
 * assemble
 * custom-fields.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { Field, FieldDescription, FieldError, FieldLabel } from "@/components/ui/field";
import EmployeeSearch from "@/components/admin/users/form/EmployeeSearch";
import { Control, FieldValues, Path } from "react-hook-form";
import RoleSearch from "@/components/admin/users/form/RoleSearch";
import { CustomField } from "@/components/custom-ui/form/fields";


function EmployeeLookupField<TFieldValues extends FieldValues, TTransformedValues extends FieldValues>(
    { fieldName, formControl, disabled }
    :
    {
        fieldName: Path<TFieldValues>,
        formControl: Control<TFieldValues, any, TTransformedValues>,
        disabled: boolean
    }
) {
    return <CustomField
        fieldName={ fieldName }
        formControl={ formControl }
        disabled={ disabled }
        renderAction={ ( { field, fieldState } ) => (
            <Field data-invalid={ fieldState.invalid }>
                <FieldLabel htmlFor={ `${ fieldName }-field` }>Employee</FieldLabel>
                <EmployeeSearch field={ field }
                                disabled={ disabled }/>
                <FieldDescription>
                    Connect an employee to user. Leave this field empty if you want to
                    assign a employee later.
                </FieldDescription>
                { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                </FieldError> }
            </Field>
        ) }/>
}

function RolesLookupField<TFieldValues extends FieldValues, TTransformedValues extends FieldValues>(
    { fieldName, formControl, disabled }
    :
    {
        fieldName: Path<TFieldValues>,
        formControl: Control<TFieldValues, any, TTransformedValues>,
        disabled: boolean
    }
) {
    return <CustomField
        fieldName={ fieldName }
        formControl={ formControl }
        disabled={ disabled }
        renderAction={ ( { field, fieldState } ) => (
            <Field data-invalid={ fieldState.invalid }>
                <FieldLabel htmlFor={ `${ fieldName }-field` }>Roles</FieldLabel>
                <RoleSearch field={ field }
                            disabled={ disabled }/>
                <FieldDescription>
                    This field is required
                </FieldDescription>
                { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                </FieldError> }
            </Field>
        ) }/>
}

export {
    EmployeeLookupField,
    RolesLookupField
}