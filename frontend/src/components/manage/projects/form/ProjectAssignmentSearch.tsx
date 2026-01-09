/*
 * assemble
 * ProjectAssignmentSearch.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";

import Lookup, { LookupItem } from "@/components/custom-ui/form/Lookup";
import { ControllerRenderProps, FieldValues } from "react-hook-form";
import { useCallback, useEffect, useState } from "react";
import { LayersIcon } from "lucide-react";
import {
    useGetAllProjectAssignmentsByEmployeeId, useGetOwnProjectAssignments,
} from "@/api/rest/generated/query/project-assignments/project-assignments";
import { ProjectAssignmentDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";

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

type ProjectAssignmentSearchProps = {
    field: ControllerRenderProps<FieldValues, string>,
    disabled: boolean,
    selectedId?: string,
    employeeId: string
}

type DataProps = {
    data: ProjectAssignmentDTO[] | undefined,
    isError: boolean,
    isLoading: boolean
}

export function AdminProjectAssignmentSearch( {
                                                  field,
                                                  disabled,
                                                  selectedId,
                                                  employeeId
                                              }: ProjectAssignmentSearchProps
) {
    const { data, isError, isLoading } = useGetAllProjectAssignmentsByEmployeeId( employeeId );

    return <ProjectAssignmentSearch { ...{ field, disabled, selectedId, employeeId, data, isError, isLoading } }/>
}

export function UserProjectAssignmentSearch( {
                                                 field,
                                                 disabled,
                                                 selectedId,
                                                 employeeId
                                             }: ProjectAssignmentSearchProps ) {
    const { data, isError, isLoading } = useGetOwnProjectAssignments();

    return <ProjectAssignmentSearch { ...{ field, disabled, selectedId, employeeId, data, isError, isLoading } }
    />;
}

function ProjectAssignmentSearch(
    {
        field,
        disabled,
        selectedId,
        data,
        isError,
        isLoading
    }:
        ProjectAssignmentSearchProps & DataProps
) {
    const [ state, setState ] = useState<SearchState>( initialState );

    const items: LookupItem[] | null = data != null ? data
        .filter( assignment => assignment.active )
        .map( assignment => ( {
            id: assignment.project.id,
            result: assignment.project.name,
            searchTerm: assignment.project.name
        } ) ) : null;


    useEffect( () => {
        const callSetState = ( selectedValue: LookupItem | null, searchTerm: string ) => setState( prev => ( {
            ...prev,
            selectedValue,
            searchTerm,
        } ) );

        if ( items != null ) {
            const selectedValue = items ? items.find( item => item.id === selectedId ) || null : null;
            callSetState( selectedValue || null, selectedValue?.searchTerm || "" );
        }
    }, [ items, selectedId ] )


    const onSearch = useCallback(
        ( searchTerm: string ) => {
            setState( ( prev ) => (
                { ...prev, searchTerm: searchTerm.trim() }
            ) );
        }
        , []
    );


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
        placeholder={ "Search assigned projects..." }
        heading={ "Assigned Projects" }
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