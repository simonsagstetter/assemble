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
import { useLiveLookup } from "@/hooks/use-lookup";
import { EmployeeRefDTO, UserRefDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { ErrorType } from "@/services/rest/axios-instance";
import { useSearchUnlinkedUsers } from "@/api/rest/generated/query/user-management/user-management";
import LookupBuilder from "@/components/custom-ui/form/lookup-builder";
import { useSearchAllEmployees, useSearchUnlinkedEmployees } from "@/api/rest/generated/query/employees/employees";

function UserLookupField<TFieldValues extends FieldValues, TTransformedValues extends FieldValues>(
    { fieldName, formControl, disabled }
    :
    Readonly<{
        fieldName: Path<TFieldValues>,
        formControl: Control<TFieldValues, unknown, TTransformedValues>,
        disabled: boolean
    }>
) {

    const result = useLiveLookup<UserRefDTO[], ErrorType<unknown>>( {
        searchAction: useSearchUnlinkedUsers,
        setLookupItem: ( user ) => ( {
            id: user.id,
            item: user,
            key: "username",
            searchTerm: user.username,
        } )
    } );
    return <CustomField
        fieldName={ fieldName }
        formControl={ formControl }
        renderAction={ ( { field, fieldState } ) => (
            <Field data-invalid={ fieldState.invalid }>
                <FieldLabel>User</FieldLabel>
                <LookupBuilder<UserRefDTO>
                    field={ field }
                    { ...result }
                    options={ {
                        emptyMessage: "No users found",
                        errorMessage: "Failed to load users",
                        heading: "Users",
                        input: {
                            placeholder: "Search for users..."
                        },
                        button: {
                            placeholder: "Select a user",
                            disabled
                        }
                    } }
                />
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
    { fieldName, formControl, disabled, excludeValues, onSelectHandler, initialSearchTerm }
    :
    Readonly<{
        fieldName: Path<TFieldValues>,
        formControl: Control<TFieldValues, unknown, TTransformedValues>,
        disabled: boolean,
        excludeValues: string[],
        onSelectHandler?: ( value: string ) => void,
        initialSearchTerm?: string
    }>
) {

    const result = useLiveLookup<EmployeeRefDTO[], ErrorType<unknown>>( {
        searchAction: useSearchAllEmployees,
        setLookupItem: ( employee ) => ( {
            id: employee.id,
            item: employee,
            key: "fullname",
            searchTerm: employee.fullname,
            disabled: excludeValues.includes( employee.id )
        } ),
        initialSearchTerm,
    } );

    return <CustomField
        fieldName={ fieldName }
        formControl={ formControl }
        renderAction={ ( { field, fieldState } ) => (
            <Field data-invalid={ fieldState.invalid }>
                <FieldLabel>Employee</FieldLabel>
                <LookupBuilder<EmployeeRefDTO>
                    field={ field }
                    { ...result }
                    options={ {
                        emptyMessage: "No employees found",
                        errorMessage: "Failed to load employees",
                        heading: "Employees",
                        input: {
                            placeholder: "Search for employees..."
                        },
                        button: {
                            placeholder: "Select employee",
                            disabled
                        },
                        selectAction: onSelectHandler
                    } }
                />
                { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                </FieldError> }
            </Field>
        ) }/>
}

function UnlinkedEmployeeLookupField<TFieldValues extends FieldValues, TTransformedValues extends FieldValues>(
    { fieldName, formControl, disabled }
    :
    Readonly<{
        fieldName: Path<TFieldValues>,
        formControl: Control<TFieldValues, unknown, TTransformedValues>,
        disabled: boolean
    }>
) {
    const result = useLiveLookup<EmployeeRefDTO[], ErrorType<unknown>>( {
        searchAction: useSearchUnlinkedEmployees,
        setLookupItem: ( emplpoyee ) => ( {
            id: emplpoyee.id,
            item: emplpoyee,
            key: "fullname",
            searchTerm: emplpoyee.fullname
        } )
    } );

    return <CustomField
        fieldName={ fieldName }
        formControl={ formControl }
        renderAction={ ( { field, fieldState } ) => (
            <Field data-invalid={ fieldState.invalid }>
                <FieldLabel>Employee</FieldLabel>
                <LookupBuilder<EmployeeRefDTO>
                    field={ field }
                    { ...result }
                    options={ {
                        emptyMessage: "No unlinked employees found",
                        errorMessage: "Failed to load employees",
                        heading: "Unlinked employees",
                        input: {
                            placeholder: "Search for unlinked employees..."
                        },
                        button: {
                            placeholder: "Select an employee",
                            disabled
                        }
                    } }
                />
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