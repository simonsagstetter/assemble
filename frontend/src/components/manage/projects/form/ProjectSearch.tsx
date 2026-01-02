/*
 * assemble
 * ProjectSearch.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";

import Lookup, { LookupItem } from "@/components/custom-ui/form/Lookup";
import { ControllerRenderProps, FieldValues } from "react-hook-form";
import { useCallback, useState } from "react";
import { LayersIcon } from "lucide-react";
import { useSearchAllProjects } from "@/api/rest/generated/query/projects/projects";

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

type ProjectSearchProps = {
    field: ControllerRenderProps<FieldValues, string>,
    disabled: boolean,
    excludeValues: string[]
}


export default function ProjectSearch( { field, disabled, excludeValues }: ProjectSearchProps ) {
    const [ state, setState ] = useState<SearchState>( initialState );
    const { data, isError, isLoading } = useSearchAllProjects(
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
        .map( project => ( {
            id: project.id,
            result: project.name,
            Icon: <LayersIcon/>,
            searchTerm: state.searchTerm,
            disabled: excludeValues.includes( project.id )
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
        placeholder={ "Search projects..." }
        heading={ "Projects" }
        emptyMessage={ "No projects found." }
        searchTerm={ state.searchTerm }
        searchCallbackAction={ onSearch }
        selectCallbackAction={ onSelect }
        isLoading={ isLoading }
        isError={ isError }
        disabled={ disabled }
        items={ items }
        selectedValue={ state.selectedValue }
        delay={ 500 }
        Icon={ <LayersIcon/> }
    />
}