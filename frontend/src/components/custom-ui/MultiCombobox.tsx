/*
 * assemble
 * MultiCombobox.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { ControllerRenderProps, FieldValues } from "react-hook-form";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Button } from "@/components/ui/button";
import { Check, ChevronsUpDown } from "lucide-react";
import { Command, CommandGroup, CommandItem } from "@/components/ui/command";
import { ReactNode } from "react";


type MultiComboboxProps = {
    placeholder: string,
    heading: string,
    field: ControllerRenderProps<FieldValues, string>,
    options: string[],
    disabled: boolean,
    Icon?: ReactNode
}

export function MultiCombobox( {
                                   placeholder,
                                   heading,
                                   field,
                                   options,
                                   disabled,
                                   Icon
                               }: MultiComboboxProps
) {

    const onSelectHandle = ( option: string ) => {
        if ( field.value != null ) {
            if ( field.value.includes( option ) ) {
                field.onChange( field.value.filter( ( v: string ) => v !== option ) );
            } else {
                field.onChange( [ ...field.value, option ] );
            }
        } else {
            field.onChange( [ option ] );
        }
    }

    return (
        <Popover>
            <PopoverTrigger asChild>
                <Button
                    variant="outline"
                    role="combobox"
                    className="justify-between font-normal w-full"
                    disabled={ disabled }
                >
                    { field.value && field.value.length > 0
                        ? field.value.join( ", " )
                        : placeholder }
                    <ChevronsUpDown className="opacity-50"/>
                </Button>
            </PopoverTrigger>
            <PopoverContent className="p-0 w-(--radix-popover-trigger-width)">
                <Command>
                    <CommandGroup heading={ heading }>
                        { options.map( ( option ) => (
                            <CommandItem
                                key={ option }
                                onSelect={ onSelectHandle }
                                className={ "flex flex-row justify-between items-center" }
                            >
                                <div className={ "inline-flex gap-4 items-center" }>
                                    { Icon != null ? Icon : null }
                                    { option }
                                </div>
                                <Check
                                    className={ `mr-2 h-4 w-4 ${
                                        field.value && field.value.includes( option )
                                            ? "opacity-100"
                                            : "opacity-0"
                                    }` }
                                />
                            </CommandItem>
                        ) ) }
                    </CommandGroup>
                </Command>
            </PopoverContent>
        </Popover>
    );
}
