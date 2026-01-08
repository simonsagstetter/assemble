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
import { Control, FieldValues, Path } from "react-hook-form";
import RoleSearch from "@/components/admin/users/form/RoleSearch";
import { CustomField } from "@/components/custom-ui/form/fields";

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
    RolesLookupField
}