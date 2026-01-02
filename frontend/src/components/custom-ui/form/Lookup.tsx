/*
 * assemble
 * Lookup.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client"

import { Check, ChevronsUpDown, XIcon } from "lucide-react"

import { Button } from "@/components/ui/button"
import { Command, CommandEmpty, CommandGroup, CommandInput, CommandItem, CommandList, } from "@/components/ui/command"
import { Popover, PopoverContent, PopoverTrigger, } from "@/components/ui/popover"
import { Spinner } from "@/components/ui/spinner";
import { memo, ReactNode, useCallback, useRef } from "react";
import { Timeout } from "@radix-ui/primitive";

export type LookupItem = {
    id: string,
    result: string,
    searchTerm: string,
    disabled?: boolean,
}

type LookupProps = {
    open: boolean,
    setOpenAction: ( open: boolean ) => void;
    placeholder: string,
    heading: string,
    emptyMessage: string,
    searchTerm: string,
    searchCallbackAction: ( searchTerm: string ) => void,
    selectCallbackAction: ( selectedValue: LookupItem ) => void,
    isLoading: boolean,
    isError: boolean,
    disabled: boolean,
    items: LookupItem[] | null,
    selectedValue: LookupItem | null,
    delay?: number,
    Icon?: ReactNode,
}

const Lookup = memo( function Lookup(
    {
        open = false,
        setOpenAction,
        heading,
        placeholder,
        emptyMessage,
        searchTerm,
        searchCallbackAction,
        selectCallbackAction,
        isLoading,
        isError,
        disabled,
        items,
        selectedValue,
        delay = 100,
        Icon
    }: LookupProps
) {
    const debounceTimeout = useRef<Timeout>( null );

    const handleOnValueChange = useCallback( ( value: string ) => {
        const timeout = debounceTimeout.current;
        if ( timeout ) clearTimeout( timeout );

        debounceTimeout.current = setTimeout( () => {
            searchCallbackAction( value );
        }, delay )
    }, [ searchCallbackAction, delay ] );

    return (
        <Popover open={ open } onOpenChange={ setOpenAction }>
            <PopoverTrigger asChild>
                <Button
                    variant="outline"
                    role="combobox"
                    aria-expanded={ open }
                    disabled={ disabled }
                    className="justify-between font-normal w-full"
                >
                    { selectedValue?.result ?
                        <div className={ "inline-flex gap-2 items-center " }>
                            { Icon } { selectedValue.result }
                        </div>
                        : placeholder }
                    <ChevronsUpDown className="opacity-50"/>
                </Button>
            </PopoverTrigger>
            <PopoverContent className="p-0 w-(--radix-popover-trigger-width)">
                <Command value={ searchTerm } className={ "relative w-full" }>
                    <CommandInput placeholder={ placeholder } onValueChange={ handleOnValueChange }
                                  className={ "pr-9" }/>
                    { isLoading ? <div className={ "absolute right-3 top-3" }>
                        <Spinner/>
                    </div> : null }
                    <CommandList>
                        <CommandEmpty>{ emptyMessage }</CommandEmpty>
                        { isError ? <CommandEmpty>Failed to load items</CommandEmpty> : null }
                        { items != null && items.length > 0 ?
                            <CommandGroup heading={ heading }>
                                { items.map( ( item ) => {
                                    const { id, result, searchTerm, disabled } = item;
                                    return <CommandItem
                                        key={ id }
                                        value={ searchTerm }
                                        onSelect={ () => !disabled ? selectCallbackAction( item ) : null }
                                        className={ "flex flex-row items-center justify-between" }
                                    >
                                        <div className={ "inline-flex gap-4 items-center" }>
                                            { Icon != null ? Icon : null }
                                            { result }
                                        </div>
                                        { disabled ?
                                            <div className={ "flex flex-row gap-2 items-center" }>
                                                <span className={ "text-sm" }>Already assigned</span>
                                                <XIcon/>
                                            </div>
                                            :
                                            <Check
                                                className={ `mr-2 h-4 w-4 ${
                                                    selectedValue?.result == result
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
    )
} );

export default Lookup;
