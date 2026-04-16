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
import { CustomField } from "@/components/custom-ui/form/fields";
import { MultiCombobox } from "@/components/custom-ui/form/multi-combobox";
import { UserCogIcon } from "lucide-react";
import { UserRolesItem } from "@/api/rest/generated/query/openAPIDefinition.schemas";

type RolesLookupFieldProps<TFieldValues extends FieldValues, TTransformedValues extends FieldValues> = {
    fieldName: Path<TFieldValues>,
    formControl: Control<TFieldValues, unknown, TTransformedValues>,
    disabled: boolean
}

function RolesLookupField<TFieldValues extends FieldValues, TTransformedValues extends FieldValues>(
    { fieldName, formControl, disabled }
    :
    Readonly<RolesLookupFieldProps<TFieldValues, TTransformedValues>>
) {
    return <CustomField
        fieldName={ fieldName }
        formControl={ formControl }
        renderAction={ ( { field, fieldState } ) => (
            <Field data-invalid={ fieldState.invalid }>
                <FieldLabel>Roles</FieldLabel>
                <MultiCombobox
                    disabled={ disabled }
                    field={ field }
                    heading={ "User Roles" }
                    placeholder={ "Select roles..." }
                    Icon={ <UserCogIcon/> }
                    options={ Object.values( UserRolesItem ) }
                />
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