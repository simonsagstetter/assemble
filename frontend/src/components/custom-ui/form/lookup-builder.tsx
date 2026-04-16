/*
 * assemble
 * lookup-builder.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";
import {
    Dispatch,
    ForwardRefExoticComponent,
    RefAttributes,
    SetStateAction,
    useCallback,
    useEffect,
    useMemo,
    useRef,
    useState
} from "react";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Button } from "@/components/ui/button";
import { Check, ChevronsUpDown, LucideProps, XIcon } from "lucide-react";
import { Command, CommandEmpty, CommandGroup, CommandInput, CommandItem, CommandList } from "@/components/ui/command";
import { Timeout } from "@radix-ui/primitive";
import { ControllerRenderProps, FieldValues } from "react-hook-form";
import { Spinner } from "@/components/ui/spinner";

type NestedPaths<T, Prefix extends string = ''> = {
    [K in keyof T]: T[K] extends string
        ? `${ Prefix }${ K & string }`
        : T[K] extends object
            ? NestedPaths<T[K], `${ Prefix }${ K & string }.`> | `${ Prefix }${ K & string }`
            : never;
}[keyof T];

export type LookupItem<T> = {
    id: string,
    item: T,
    key: NestedPaths<T>,
    searchTerm: string,
    disabled?: boolean,
}

type LookupBuilderOptions = {
    emptyMessage?: string;
    errorMessage?: string;
    Icon?: ForwardRefExoticComponent<Omit<LucideProps, "ref"> & RefAttributes<SVGSVGElement>>;
    heading?: string;
    button?: {
        placeholder?: string;
        disabled?: boolean;
    }
    input?: {
        placeholder?: string;
        delay?: number;
    },
    filterAction?: ( value: string, search: string, keywords: string[] | undefined ) => number;
    selectAction?: ( id: string ) => void;
}

const LookupBuilderDefaultOptions = {
    emptyMessage: "No results found",
    errorMessage: "Failed to load items",
    heading: "Results",
    input: {
        placeholder: "Search...",
        delay: 200
    },
    button: {
        placeholder: "Select an item",
        disabled: false
    },
    filterAction: ( value, search ) => ( value.includes( search ) ? 1 : 0 )
} satisfies LookupBuilderOptions;

type LookupBuilderProps<TData> = {
    field: ControllerRenderProps<FieldValues, string>,
    options?: LookupBuilderOptions;
    data: LookupItem<TData>[],
    isError: boolean,
    isLoading: boolean,
    searchTerm?: string,
    setSearchTerm?: Dispatch<SetStateAction<string>>;
}

function LookupBuilder<TData>(
    {
        field,
        options = LookupBuilderDefaultOptions,
        data,
        isError,
        isLoading,
        setSearchTerm,
    }:
    Readonly<LookupBuilderProps<TData>>
) {
    const isLiveLookup = setSearchTerm != undefined;

    const [ open, setOpen ] = useState( false );

    const selectedValue = useMemo( () => {
        if ( !field.value || data.length == 0 ) return null;
        return data.find( item => item.id === field.value );
    }, [ field.value, data ] );

    const timeout = useRef<Timeout>( null );

    const handleValueChange = useCallback( ( search: string ) => {
        if ( !isLiveLookup ) return;
        if ( timeout.current ) clearTimeout( timeout.current );
        timeout.current = setTimeout( () => {
            setSearchTerm( search );
        }, 200 );
    }, [ setSearchTerm, isLiveLookup ] );

    const handleSelectItem = useCallback( ( item: LookupItem<TData> ) => {
        if ( item.id === selectedValue?.id ) {
            setOpen( true );
            field.onChange( "" );
            options.selectAction?.( "" );
        } else {
            setOpen( false );
            field.onChange( item.id );
            options.selectAction?.( item.id );
        }
    }, [ setOpen, field, selectedValue, options ] )

    useEffect( () => {
        return () => {
            if ( timeout.current ) {
                clearTimeout( timeout.current )
            }
        }
    }, [] );

    return <Popover open={ open } onOpenChange={ setOpen }>
        <PopoverTrigger asChild>
            <Button
                variant={ "outline" }
                role={ "combobox" }
                aria-expanded={ open }
                disabled={ options.button?.disabled }
                className={ "justify-between font-normal w-full" }>
                { selectedValue?.searchTerm ?
                    <div className={ "inline-flex gap-2 items-center " }>
                        { options.Icon ? <options.Icon/> : null } { selectedValue.searchTerm }
                    </div>
                    : options.button?.placeholder }
                <ChevronsUpDown className="opacity-50"/>
            </Button>
        </PopoverTrigger>
        <PopoverContent className={ "p-0 w-(--radix-popover-trigger-width)" }>
            <Command shouldFilter={ !isLiveLookup } className={ "relative w-full" }
                     filter={ options.filterAction }>
                <CommandInput
                    onValueChange={ ( search ) => isLiveLookup ? handleValueChange( search ) : null }
                    placeholder={ options.input?.placeholder }
                    className={ "pr-9" }
                />
                { isLoading ? <div className={ "absolute right-3 top-3" }>
                    <Spinner/>
                </div> : null }
                <CommandList>
                    { isError ?
                        <CommandEmpty>
                            { options.errorMessage }
                        </CommandEmpty>
                        :
                        <CommandEmpty>{ options?.emptyMessage }</CommandEmpty>
                    }
                    { data && data.length > 0 ?
                        <CommandGroup heading={ options.heading ?? "Results" }>
                            { data.map( result => {
                                const { id, key, item, searchTerm, disabled } = result;
                                const label = getNestedValue( item, key );
                                return <CommandItem key={ id }
                                                    value={ searchTerm }
                                                    onSelect={ () => !disabled ? handleSelectItem( result ) : null }
                                                    className={ "flex flex-row items-center justify-between bg-background! hover:bg-accent! hover:text-accent-foreground!" }>
                                    <div className={ "inline-flex gap-4 items-center" }>
                                        { options.Icon ? <options.Icon/> : null }
                                        { label }
                                    </div>
                                    { disabled ?
                                        <div className={ "flex flex-row gap-2 items-center" }>
                                            <span className={ "text-sm" }>Already assigned</span>
                                            <XIcon/>
                                        </div>
                                        :
                                        <Check
                                            className={ `mr-2 h-4 w-4 ${
                                                selectedValue && getNestedValue( selectedValue.item, key ) === label
                                                    ? "opacity-100"
                                                    : "opacity-0"
                                            }` }
                                        />
                                    }
                                </CommandItem>
                            } ) }
                        </CommandGroup>
                        : null }
                </CommandList>
            </Command>

        </PopoverContent>

    </Popover>
}

function getNestedValue<T>( obj: T, path: string ): string {
    const value = path.split( '.' ).reduce( ( current: any, key ) => current?.[ key ], obj );
    return String( value ?? '' );
}

export default LookupBuilder;