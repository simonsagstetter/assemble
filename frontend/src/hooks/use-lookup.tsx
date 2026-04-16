/*
 * assemble
 * use-lookup.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";
import { Dispatch, SetStateAction, useState } from "react";
import { UseQueryOptions, UseQueryResult } from "@tanstack/react-query";
import { LookupItem } from "@/components/custom-ui/form/lookup-builder";
import { ArrayElement } from "@/types/default.types";

type LookupBaseProps<TData> = {
    setLookupItem: ( item: ArrayElement<TData> ) => LookupItem<ArrayElement<TData>>;
    setFilter?: ( item: ArrayElement<TData> ) => boolean;
}

type ProcessLookupProps<TData, TError> = {
    searchTerm?: string;
    setSearchTerm?: Dispatch<SetStateAction<string>>;
} & LookupBaseProps<TData> & UseQueryResult<TData, TError>;

function processLookup<TData, TError>( props: Readonly<ProcessLookupProps<TData, TError>> ) {
    const { data: queryData, isLoading, isError, searchTerm, setSearchTerm, setLookupItem, setFilter } = props;

    let data: LookupItem<ArrayElement<TData>>[] = [];

    if ( queryData && Array.isArray( queryData ) && queryData.length !== 0 ) {

        const filteredData = setFilter ? queryData
            .filter( setFilter ) : queryData;

        data = filteredData.map( setLookupItem );

    }

    return {
        searchTerm,
        setSearchTerm,
        isLoading,
        data,
        isError,
    }
}

type StaticLookupHook<TData, TError> = (
    options?: {
        query?: Partial<UseQueryOptions<TData, TError, TData>>
    }
) => UseQueryResult<TData, TError>;

type StaticLookupProps<TData, TError> = {
    searchAction: StaticLookupHook<TData, TError>
} & LookupBaseProps<TData>;

function useStaticLookup<TData, TError>( props: Readonly<StaticLookupProps<TData, TError>> ) {
    const { searchAction, setFilter, setLookupItem } = props;
    const result = searchAction();

    return processLookup( {
        ...result,
        setLookupItem,
        setFilter
    } )
}

type StaticLookupWithParamHook<TData, TError> = (
    param: string,
    options?: {
        query?: Partial<UseQueryOptions<TData, TError, TData>>
    }
) => UseQueryResult<TData, TError>;

type StaticLookupWithParamProps<TData, TError> = {
    searchAction: StaticLookupWithParamHook<TData, TError>,
    param: string
} & LookupBaseProps<TData>;

function useStaticLookupWithParams<TData, TError>( props: Readonly<StaticLookupWithParamProps<TData, TError>> ) {
    const { searchAction, setFilter, param, setLookupItem } = props;
    const result = searchAction(
        param
    )

    return processLookup( {
        ...result,
        setLookupItem,
        setFilter
    } )
}

type LiveLookupHook<TData, TError> = (
    param: string,
    options?: {
        query?: Partial<UseQueryOptions<TData, TError, TData>>
    }
) => UseQueryResult<TData, TError>;

type LiveLookupProps<TData, TError> =
    {
        searchAction: LiveLookupHook<TData, TError>,
        initialSearchTerm?: string,
    } & LookupBaseProps<TData>;

function useLiveLookup<TData, TError>(
    { searchAction, initialSearchTerm, setFilter, setLookupItem }:
    Readonly<LiveLookupProps<TData, TError>>
) {
    const [ searchTerm, setSearchTerm ] = useState( initialSearchTerm ?? "" );

    const result = searchAction( searchTerm, { query: { enabled: !!searchTerm } } );

    return processLookup<TData, TError>( {
        ...result,
        searchTerm,
        setSearchTerm,
        setLookupItem,
        setFilter
    } )
}

export { useStaticLookup, useLiveLookup, useStaticLookupWithParams };