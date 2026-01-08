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
import ProjectSearch from "@/components/manage/projects/form/ProjectSearch";
import {
    AdminProjectAssignmentSearch,
    UserProjectAssignmentSearch
} from "@/components/manage/projects/form/ProjectAssignmentSearch";
import { ReactNode } from "react";


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

function ProjectAssignmentLookupField<TFieldValues extends FieldValues, TTransformedValues extends FieldValues>(
    { fieldName, formControl, disabled, selectedId, employeeId, isRestrictedSerach = true, children }
    :
    {
        fieldName: Path<TFieldValues>,
        formControl: Control<TFieldValues, any, TTransformedValues>,
        disabled: boolean,
        selectedId?: string,
        employeeId: string,
        children?: ReactNode,
        isRestrictedSerach?: boolean
    }
) {
    return <CustomField
        fieldName={ fieldName }
        formControl={ formControl }
        renderAction={ ( { field, fieldState } ) => (
            <Field data-invalid={ fieldState.invalid }>
                <FieldLabel htmlFor={ `${ fieldName }-field` }>Project</FieldLabel>
                { isRestrictedSerach ?
                    <UserProjectAssignmentSearch field={ field }
                                                 disabled={ disabled }
                                                 selectedId={ selectedId } employeeId={ employeeId }/>
                    :
                    <AdminProjectAssignmentSearch field={ field }
                                                  disabled={ disabled }
                                                  selectedId={ selectedId } employeeId={ employeeId }/>

                }
                { children ? <FieldDescription>{ children }</FieldDescription> : null }
                { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                </FieldError> }
            </Field>
        ) }/>
}

export {
    ProjectLookupField,
    ProjectAssignmentLookupField
}