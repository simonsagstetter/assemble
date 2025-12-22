/*
 * assemble
 * UserSearch.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";

import { useCallback, useState } from "react";
import { UserIcon } from "lucide-react";
import Lookup, { type LookupItem } from "@/components/custom-ui/form/Lookup";
import { ControllerRenderProps, FieldValues } from "react-hook-form";
import { useSearchUnlinkedUsers } from "@/api/rest/generated/query/user-management/user-management";


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
    disabled: boolean
}

export default function UserSearch( { field, disabled }: EmployeeSearchProps ) {
    const [ state, setState ] = useState<SearchState>( initialState );
    const { data, isError, isLoading } = useSearchUnlinkedUsers(
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

    const items: LookupItem[] | null = data != null ? data.map( user => ( {
        id: user.id,
        result: user.username,
        Icon: <UserIcon/>,
        searchTerm: state.searchTerm
    } ) ) : null;

    const onSelect = useCallback( ( selectedValue: LookupItem ) => {
        if ( selectedValue.id === state.selectedValue?.id ) {
            setState( ( prev ) => (
                { ...prev, selectedValue: null, open: true }
            ) );
            field.onChange( "" );
        } else {
            setState( ( prev ) => (
                { ...prev, selectedValue, open: false }
            ) );
            field.onChange( selectedValue.id );
        }
    }, [ field, state.selectedValue ] )


    return <Lookup
        open={ state.open }
        setOpenAction={ ( open ) => setState( ( prev ) => ( { ...prev, open } ) ) }
        placeholder={ "Search users..." }
        heading={ "Unlinked Users" }
        emptyMessage={ "No users found" }
        searchTerm={ state.searchTerm }
        searchCallbackAction={ onSearch }
        selectCallbackAction={ onSelect }
        isLoading={ isLoading }
        isError={ isError }
        disabled={ disabled }
        items={ items }
        selectedValue={ state.selectedValue }
        delay={ 500 }
        Icon={ <UserIcon/> }
    />
}
