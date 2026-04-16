/*
 * assemble
 * custom-fields.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";
import { Field, FieldDescription, FieldError, FieldLabel } from "@/components/ui/field";
import { Control, FieldValues, Path } from "react-hook-form";
import { CustomField } from "@/components/custom-ui/form/fields";
import { ReactNode } from "react";
import LookupBuilder from "@/components/custom-ui/form/lookup-builder";
import { ProjectAssignmentDTO, ProjectDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { useSearchAllProjects } from "@/api/rest/generated/query/projects/projects";
import { ErrorType } from "@/services/rest/axios-instance";
import { useLiveLookup, useStaticLookup, useStaticLookupWithParams } from "@/hooks/use-lookup";
import {
    useGetAllProjectAssignmentsByEmployeeId,
    useGetOwnProjectAssignments
} from "@/api/rest/generated/query/project-assignments/project-assignments";


function ProjectLookupField<TFieldValues extends FieldValues, TTransformedValues extends FieldValues>(
    { fieldName, formControl, disabled, excludeValues }
    :
    Readonly<{
        fieldName: Path<TFieldValues>,
        formControl: Control<TFieldValues, unknown, TTransformedValues>,
        disabled: boolean,
        excludeValues: string[]
    }>
) {
    const result = useLiveLookup<ProjectDTO[], ErrorType<unknown>>( {
        searchAction: useSearchAllProjects,
        setFilter: ( project ) => project.active,
        setLookupItem: ( project ) => ( {
            id: project.id,
            item: project,
            key: "name",
            searchTerm: project.name,
            disabled: excludeValues.includes( project.id )
        } )
    } )

    return <CustomField
        fieldName={ fieldName }
        formControl={ formControl }
        renderAction={ ( { field, fieldState } ) => (
            <Field data-invalid={ fieldState.invalid }>
                <FieldLabel>Project</FieldLabel>
                <LookupBuilder<ProjectDTO>
                    field={ field }
                    { ...result }
                    options={ {
                        emptyMessage: "No projects found",
                        errorMessage: "Failed to load projects",
                        heading: "Projects",
                        input: {
                            placeholder: "Search for projects..."
                        },
                        button: {
                            placeholder: "Select a project",
                            disabled
                        },
                    } }
                />
                { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                </FieldError> }
            </Field>
        ) }/>
}

function UserProjectAssignmentLookupField<TFieldValues extends FieldValues, TTransformedValues extends FieldValues>(
    { fieldName, formControl, disabled, children }
    :
    Readonly<{
        fieldName: Path<TFieldValues>,
        formControl: Control<TFieldValues, unknown, TTransformedValues>,
        disabled: boolean,
        children?: ReactNode,
    }>
) {
    const result = useStaticLookup<ProjectAssignmentDTO[], ErrorType<unknown>>( {
        searchAction: useGetOwnProjectAssignments,
        setFilter: ( assignment ) => assignment.active,
        setLookupItem: ( assignment ) => ( {
            id: assignment.project.id,
            item: assignment,
            key: "project.name",
            searchTerm: assignment.project.name,
        } )
    } );

    return <CustomField
        fieldName={ fieldName }
        formControl={ formControl }
        renderAction={ ( { field, fieldState } ) => (
            <Field data-invalid={ fieldState.invalid }>
                <FieldLabel>Project</FieldLabel>
                <LookupBuilder<ProjectAssignmentDTO>
                    field={ field }
                    { ...result }
                    options={ {
                        emptyMessage: "No assignments found",
                        errorMessage: "Failed to load assignments",
                        heading: "Projects",
                        input: {
                            placeholder: "Search for assignments..."
                        },
                        button: {
                            placeholder: "Select a project",
                            disabled
                        },
                    } }
                />
                { children ? <FieldDescription>{ children }</FieldDescription> : null }
                { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                </FieldError> }
            </Field>
        ) }/>
}

function AdminProjectAssignmentLookupField<TFieldValues extends FieldValues, TTransformedValues extends FieldValues>(
    { fieldName, formControl, disabled, employeeId, children }
    :
    Readonly<{
        fieldName: Path<TFieldValues>,
        formControl: Control<TFieldValues, unknown, TTransformedValues>,
        disabled: boolean,
        employeeId: string,
        children?: ReactNode,
    }>
) {
    const result = useStaticLookupWithParams<ProjectAssignmentDTO[], ErrorType<unknown>>( {
        searchAction: useGetAllProjectAssignmentsByEmployeeId,
        param: employeeId,
        setFilter: ( assignment ) => assignment.active,
        setLookupItem: ( assignment ) => ( {
            id: assignment.project.id,
            item: assignment,
            key: "project.name",
            searchTerm: assignment.project.name,
        } )
    } );

    return <CustomField
        fieldName={ fieldName }
        formControl={ formControl }
        renderAction={ ( { field, fieldState } ) => (
            <Field data-invalid={ fieldState.invalid }>
                <FieldLabel>Project</FieldLabel>
                <LookupBuilder<ProjectAssignmentDTO>
                    field={ field }
                    { ...result }
                    options={ {
                        emptyMessage: "No assignments found",
                        errorMessage: "Failed to load assignments",
                        heading: "Projects",
                        input: {
                            placeholder: "Search for assignments..."
                        },
                        button: {
                            placeholder: "Select a project",
                            disabled
                        },
                    } }
                />
                { children ? <FieldDescription>{ children }</FieldDescription> : null }
                { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                </FieldError> }
            </Field>
        ) }/>
}

export {
    ProjectLookupField,
    UserProjectAssignmentLookupField,
    AdminProjectAssignmentLookupField
}