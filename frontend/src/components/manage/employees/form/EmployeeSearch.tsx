/*
 * assemble
 * EmployeeSearch.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";

import { useCallback, useState } from "react";
import { useSearchAllEmployees } from "@/api/rest/generated/query/employees/employees";
import { IdCardIcon } from "lucide-react";
import Lookup, { type LookupItem } from "@/components/custom-ui/form/Lookup";
import { ControllerRenderProps, FieldValues } from "react-hook-form";


type SearchState = {
    open: boolean;
    searchTerm: string;
    selectedValue: LookupItem | null;
}

const initialState = {
    open: false,
    searchTerm: "",
    selectedValue: null
}

type EmployeeSearchProps = {
    field: ControllerRenderProps<FieldValues, string>,
    disabled: boolean,
    excludeValues: string[],
    onSelectAction?: ( employeeId: string ) => void
}

export default function EmployeeSearch( { field, disabled, excludeValues, onSelectAction }: EmployeeSearchProps ) {
    const [ state, setState ] = useState<SearchState>( initialState );
    const { data, isError, isLoading } = useSearchAllEmployees(
        state.searchTerm,
        {
            query: {
                enabled: !!state.searchTerm,
            }
        }
    );

    const onSearch = useCallback(
        ( searchTerm: string ) => {
            setState( ( prev ) => (
                { ...prev, searchTerm: searchTerm.trim() }
            ) );
        }
        , []
    );

    const items: LookupItem[] | null = data != null ? data
        .map( employee => ( {
            id: employee.id,
            result: employee.fullname,
            Icon: <IdCardIcon/>,
            searchTerm: state.searchTerm,
            disabled: excludeValues.includes( employee.id )
        } ) ) : null;

    const onSelect = useCallback( ( selectedValue: LookupItem ) => {
        if ( selectedValue.id === state.selectedValue?.id ) {
            setState( ( prev ) => (
                { ...prev, selectedValue: null, open: true }
            ) );
            field.onChange( "" );
            onSelectAction?.( "" );
        } else {
            setState( ( prev ) => (
                { ...prev, selectedValue, open: false }
            ) );
            field.onChange( selectedValue.id );
            onSelectAction?.( selectedValue.id );
        }
    }, [ field, state.selectedValue, onSelectAction ] )


    return <Lookup
        open={ state.open }
        setOpenAction={ ( open ) => setState( ( prev ) => ( { ...prev, open } ) ) }
        placeholder={ "Search employees..." }
        heading={ "Employees" }
        emptyMessage={ "No employees found." }
        searchTerm={ state.searchTerm }
        searchCallbackAction={ onSearch }
        selectCallbackAction={ onSelect }
        isLoading={ isLoading }
        isError={ isError }
        disabled={ disabled }
        items={ items }
        selectedValue={ state.selectedValue }
        delay={ 500 }
        Icon={ <IdCardIcon/> }
    />


}
