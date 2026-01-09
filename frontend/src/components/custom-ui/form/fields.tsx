/*
 * assemble
 * fields.tsx
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import {
    Control,
    Controller,
    ControllerFieldState,
    ControllerRenderProps,
    FieldValues,
    Path,
    UseFormStateReturn
} from "react-hook-form";
import React, {
    ComponentProps, FC,
    ForwardRefExoticComponent,
    ReactElement,
    ReactNode,
    RefAttributes,
    useState
} from "react";
import { Field, FieldContent, FieldDescription, FieldError, FieldLabel } from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import { Switch } from "@/components/ui/switch";
import { SwitchProps } from "@radix-ui/react-switch";
import { Calendar } from "@/components/ui/calendar";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { ChevronDownIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { SelectProps } from "@radix-ui/react-select";
import { Textarea } from "@/components/ui/textarea";

function CustomField<TFieldValues extends FieldValues, TTransformedValues extends FieldValues>(
    { fieldName, formControl, renderAction }
    :
    {
        fieldName: Path<TFieldValues>,
        formControl: Control<TFieldValues, unknown, TTransformedValues>,
        renderAction: ( { field, fieldState, formState }: {
            field: ControllerRenderProps<TFieldValues, Path<TFieldValues>>,
            fieldState: ControllerFieldState,
            formState: UseFormStateReturn<TFieldValues>
        } ) => ReactElement
    }
) {
    return <Controller
        name={ fieldName }
        control={ formControl }
        render={ renderAction }
    />
}

function InputField<TFieldValues extends FieldValues, TTransformedValues extends FieldValues>(
    { fieldName, formControl, label, children, ...props }
    :
        {
            fieldName: Path<TFieldValues>,
            formControl: Control<TFieldValues, unknown, TTransformedValues>,
            label: string,
            children?: ReactNode
        } & ComponentProps<"input">
) {
    return <Controller
        name={ fieldName }
        control={ formControl }
        render={ ( { field, fieldState } ) => (
            <Field data-invalid={ fieldState.invalid }>
                <FieldLabel htmlFor={ `${ fieldName }-field` }>{ label }</FieldLabel>
                <Input
                    { ...field }
                    type={ "text" }
                    { ...props }
                    id={ `${ fieldName }-field` }
                    aria-invalid={ fieldState.invalid }
                />
                { children ?
                    <FieldDescription>
                        { children }
                    </FieldDescription>
                    : null
                }

                { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                </FieldError> }
            </Field>
        ) }
    />
}

function TimeField<TFieldValues extends FieldValues, TTransformedValues extends FieldValues>(
    { fieldName, formControl, label, children, ...props }
    :
        {
            fieldName: Path<TFieldValues>,
            formControl: Control<TFieldValues, unknown, TTransformedValues>,
            label: string,
            children?: ReactNode
        } & ComponentProps<"input">
) {
    return <Controller name={ fieldName }
                       control={ formControl }
                       render={ ( { field, fieldState } ) => (
                           <Field data-invalid={ fieldState.invalid }>
                               <FieldLabel htmlFor={ `${ fieldName }-field` }>{ label }</FieldLabel>
                               <Input
                                   { ...props }
                                   { ...field }
                                   type="time"
                                   min={ "00:00" }
                                   max={ "23:59" }
                                   id={ `${ fieldName }-field` }
                                   aria-invalid={ fieldState.invalid }
                               />
                               { children ?
                                   <FieldDescription>
                                       { children }
                                   </FieldDescription>
                                   : null
                               }

                               { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }></FieldError> }
                           </Field>
                       ) }/>
}

const currencyFormatter = new Intl.NumberFormat( "de-DE", {
    style: "currency",
    currency: "EUR",
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
} );

const parseCurrencyInput = ( value: string ) => {
    const onlyDigits = value.replace( /\D/g, "" );
    if ( onlyDigits === "" ) return null;

    const numberValue = Number( onlyDigits ) / 100;

    return isNaN( numberValue ) ? null : numberValue;
}

function CurrencyField<TFieldValues extends FieldValues, TTransformedValues extends FieldValues>(
    { fieldName, formControl, label, children, ...props }
    :
        {
            fieldName: Path<TFieldValues>,
            formControl: Control<TFieldValues, unknown, TTransformedValues>,
            label: string,
            children?: ReactNode
        } & ComponentProps<"input">
) {
    return <Controller
        name={ fieldName }
        control={ formControl }
        render={ ( { field, fieldState } ) => (
            <Field data-invalid={ fieldState.invalid }>
                <FieldLabel htmlFor={ `${ fieldName }-field` }>{ label }</FieldLabel>
                <Input
                    { ...props }
                    type={ "text" }
                    autoComplete={ "off" }
                    inputMode={ "decimal" }
                    value={
                        typeof field.value === "number"
                            ? currencyFormatter.format( field.value )
                            : field.value ?? ""
                    }
                    onChange={ ( e ) => {
                        const parsed = parseCurrencyInput( e.target.value );

                        field.onChange( parsed );
                    } }
                    onFocus={ ( e ) => {
                        if ( typeof field.value === "number" ) {
                            e.target.value = String( field.value.toFixed( 2 ).replace( ".", "," ) );
                        }
                    } }
                    id={ `${ fieldName }-field` }
                    aria-invalid={ fieldState.invalid }
                />
                { children ?
                    <FieldDescription>
                        { children }
                    </FieldDescription>
                    : null
                }

                { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                </FieldError> }
            </Field>
        ) }
    />
}

function TextareaField<TFieldValues extends FieldValues, TTransformedValues extends FieldValues>(
    { fieldName, formControl, label, children, ...props }
    :
        {
            fieldName: Path<TFieldValues>,
            formControl: Control<TFieldValues, unknown, TTransformedValues>,
            label: string,
            children?: ReactNode
        } & ComponentProps<"textarea">
) {
    return <Controller
        name={ fieldName }
        control={ formControl }
        render={ ( { field, fieldState } ) => (
            <Field data-invalid={ fieldState.invalid }>
                <FieldLabel htmlFor={ `${ fieldName }-field` }>{ label }</FieldLabel>
                <Textarea
                    { ...field }
                    id={ `${ fieldName }-field` }
                    aria-invalid={ fieldState.invalid }
                    { ...props }
                />
                { children ?
                    <FieldDescription>
                        { children }
                    </FieldDescription>
                    : null
                }

                { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                </FieldError> }
            </Field>
        ) }
    />
}

function SwitchField<TFieldValues extends FieldValues, TTransformedValues extends FieldValues>(
    { fieldName, formControl, label, children, ...props }:
        {
            fieldName: Path<TFieldValues>,
            formControl: Control<TFieldValues, unknown, TTransformedValues>,
            label: string,
            children: ReactNode,
        } & React.ComponentProps<ForwardRefExoticComponent<SwitchProps & RefAttributes<HTMLButtonElement>>>
) {
    return <Controller
        name={ fieldName }
        control={ formControl }
        render={ ( { field, fieldState } ) => (
            <Field data-invalid={ fieldState.invalid } orientation={ "horizontal" }>
                <FieldContent>
                    <FieldLabel htmlFor={ `${ fieldName }-field` }>{ label }</FieldLabel>
                    <FieldDescription>
                        { children }
                    </FieldDescription>
                    { fieldState.invalid &&
                        <FieldError errors={ [ fieldState.error ] }>
                        </FieldError>
                    }

                </FieldContent>
                <Switch
                    id={ `${ fieldName }-field` }
                    className={ "cursor-pointer" }
                    name={ field.name }
                    checked={ field.value }
                    onCheckedChange={ field.onChange }
                    { ...props }/>
            </Field>
        ) }
    />
}

function CalendarField<TFieldValues extends FieldValues, TTransformedValues extends FieldValues>(
    { fieldName, formControl, label, placeholder, children, ...props }:
        {
            fieldName: Path<TFieldValues>,
            formControl: Control<TFieldValues, unknown, TTransformedValues>,
            placeholder: string,
            label: string,
            children?: ReactNode,
            disabled: boolean
        } & ComponentProps<"button">
) {
    const [ open, setOpen ] = useState( false )
    return <Controller
        name={ fieldName }
        control={ formControl }
        render={ ( { field, fieldState } ) => (
            <Field data-invalid={ fieldState.invalid }>
                <FieldLabel htmlFor={ `${ fieldName }-field` }>{ label }</FieldLabel>
                <Popover open={ open } onOpenChange={ setOpen }>
                    <PopoverTrigger asChild>
                        <Button
                            variant="outline"
                            id={ `${ fieldName }-field` }
                            className="w-48 justify-between font-normal"
                            { ...props }
                        >
                            { field.value ? field.value.toLocaleDateString() : placeholder }
                            <ChevronDownIcon/>
                        </Button>
                    </PopoverTrigger>
                    <PopoverContent className="w-auto overflow-hidden p-0" align="start">
                        <Calendar
                            mode="single"
                            selected={ field.value }
                            id={ `${ fieldName }-field` }
                            captionLayout="dropdown"
                            onSelect={ ( date ) => {
                                field.onChange( date );
                                setOpen( false );
                            } }
                        />
                    </PopoverContent>
                </Popover>
                { children ?
                    <FieldDescription>
                        { children }
                    </FieldDescription>
                    : null
                }

                { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                </FieldError> }
            </Field>
        ) }
    />
}

function SelectField<TFieldValues extends FieldValues, TTransformedValues extends FieldValues>(
    { fieldName, formControl, label, placeholder, options, children, ...props }:
        {
            fieldName: Path<TFieldValues>,
            formControl: Control<TFieldValues, unknown, TTransformedValues>,
            label: string,
            placeholder: string,
            options: { label: string, value: string, className?: string, elem?: ReactNode }[],
            children?: ReactNode,
            disabled: boolean
        } & ComponentProps<FC<SelectProps>>
) {
    return <Controller
        name={ fieldName }
        control={ formControl }
        render={ ( { field, fieldState } ) => (
            <Field data-invalid={ fieldState.invalid }>
                <FieldLabel htmlFor={ `${ fieldName }-field` }>{ label }</FieldLabel>
                <Select name={ field.name }
                        value={ field.value }
                        onValueChange={ field.onChange }
                        { ...props }>
                    <SelectTrigger id={ `${ fieldName }-field` } aria-invalid={ fieldState.invalid }>
                        <SelectValue placeholder={ placeholder }/>
                    </SelectTrigger>
                    <SelectContent>
                        { options.map( ( option ) => (
                            <SelectItem key={ option.value } value={ option.value } className={ option.className }>
                                { option.elem ? option.elem : option.label }
                            </SelectItem> ) ) }
                    </SelectContent>
                </Select>
                { children ?
                    <FieldDescription>
                        { children }
                    </FieldDescription>
                    : null
                }
                { fieldState.invalid && <FieldError errors={ [ fieldState.error ] }>
                </FieldError> }
            </Field>
        ) }
    />
}

export {
    CustomField,
    InputField,
    SwitchField,
    CalendarField,
    SelectField,
    TextareaField,
    CurrencyField,
    TimeField
}