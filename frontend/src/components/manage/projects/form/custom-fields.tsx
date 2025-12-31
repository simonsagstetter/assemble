/*
 * assemble
 * custom-fields.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { Field, FieldError, FieldLabel } from "@/components/ui/field";
import { Control, FieldValues, Path } from "react-hook-form";
import { CustomField } from "@/components/custom-ui/form/fields";
import EmployeeSearch from "@/components/manage/projects/form/EmployeeSearch";
import ProjectSearch from "@/components/manage/projects/form/ProjectSearch";


function EmployeeLookupField<TFieldValues extends FieldValues, TTransformedValues extends FieldValues>(
    { fieldName, formControl, disabled, excludeValues }
    :
    {
        fieldName: Path<TFieldValues>,
        formControl: Control<TFieldValues, any, TTransformedValues>,
        disabled: boolean,
        excludeValues: string[]
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
                                excludeValues={ excludeValues }/>
                { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                </FieldError> }
            </Field>
        ) }/>
}

function ProjectLookupField<TFieldValues extends FieldValues, TTransformedValues extends FieldValues>(
    { fieldName, formControl, disabled, excludeValues }
    :
    {
        fieldName: Path<TFieldValues>,
        formControl: Control<TFieldValues, any, TTransformedValues>,
        disabled: boolean,
        excludeValues: string[]
    }
) {
    return <CustomField
        fieldName={ fieldName }
        formControl={ formControl }
        renderAction={ ( { field, fieldState } ) => (
            <Field data-invalid={ fieldState.invalid }>
                <FieldLabel htmlFor={ `${ fieldName }-field` }>Project</FieldLabel>
                <ProjectSearch field={ field }
                               disabled={ disabled }
                               excludeValues={ excludeValues }/>
                { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                </FieldError> }
            </Field>
        ) }/>
}


export {
    EmployeeLookupField,
    ProjectLookupField
}