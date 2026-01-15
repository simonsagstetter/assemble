/*
 * assemble
 * HolidayImportFrom.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";

import { FormProvider, useForm } from "react-hook-form";
import {
    getGetImportedYearsQueryKey,
    useGetHolidaysFromYear
} from "@/api/rest/generated/query/holiday-import/holiday-import";
import useModalContext from "@/hooks/useModalContext";
import { useRouter } from "@bprogress/next/app";
import { FormActionContext } from "@/store/formActionStore";
import { Separator } from "@/components/ui/separator";
import { FormActions } from "@/components/custom-ui/form/actions";
import { ScrollArea } from "@/components/ui/scroll-area";
import { FieldDescription, FieldGroup, FieldLegend, FieldSet } from "@/components/ui/field";
import { zodResolver } from "@hookform/resolvers/zod";
import { HolidayFormData, HolidaySchema } from "@/types/holidays/holiday.types";
import { format } from "date-fns";
import { SelectField } from "@/components/custom-ui/form/fields";
import { ErrorMessage, SuccessMessage } from "@/components/custom-ui/form/messages";
import { invalidateQueries } from "@/utils/query";
import { useQueryClient } from "@tanstack/react-query";
import { getGetHolidaysByYearAndSubdivisionCodeQueryKey } from "@/api/rest/generated/query/holidays/holidays";
import { toast } from "sonner";
import { AppSettingsDTOHolidaySubdivisionCode } from "@/api/rest/generated/query/openAPIDefinition.schemas";

export default function HolidayImportFrom() {
    const modalContext = useModalContext();
    const queryClient = useQueryClient();
    const router = useRouter();
    const formId = "holiday-import-from-form";
    const now = new Date();
    const form = useForm( {
        resolver: zodResolver( HolidaySchema ),
        defaultValues: {
            year: format( now, "yyyy" ),
        }
    } );

    const isSubmitting = form.formState.isSubmitting;

    const { mutate, isError, isSuccess, isPending } = useGetHolidaysFromYear()

    const disabled = isPending || isSubmitting || isSuccess;

    const handleImportHolidays = ( data: HolidayFormData ) => {
        mutate(
            {
                year: data.year,
            },
            {
                onSuccess: async () => {
                    const subdivisionCodes = Object.keys( AppSettingsDTOHolidaySubdivisionCode );
                    await invalidateQueries( queryClient, [
                        ...subdivisionCodes.map( subdivisionCode =>
                            getGetHolidaysByYearAndSubdivisionCodeQueryKey( {
                                subdivisionCode: "DE-" + subdivisionCode,
                                year: form.getValues( "year" )
                            } ) as readonly string[]
                        ),
                        getGetImportedYearsQueryKey()
                    ] );

                    toast.success( "Success", {
                        description: "Holidays were imported",
                    } );

                    form.clearErrors();
                    handleCancel();
                },
                onError: () => {
                    form.setError( "root", {
                        type: "manual",
                        message: "Could not import holidays. Please try again later. If the problem persists, please contact the administrator."
                    } );
                }
            }
        )
    }


    const handleCancel = () => {
        router.push( `/app/admin/settings/holidays?year=${ form.getValues( "year" ) }` );
    }

    return <FormActionContext.Provider
        value={ { isSuccess, isPending, isError, handleCancel, disableOnSuccess: true } }>
        <FormProvider { ...form }>
            <form id={ formId } onSubmit={ form.handleSubmit( handleImportHolidays ) } className="space-y-8">
                <ScrollArea className={ `${ modalContext ? "h-auto my-0" : "" }` }>
                    <FieldGroup className={ "py-4 px-8" }>
                        <FieldSet>
                            <FieldLegend>Import Details</FieldLegend>
                            <FieldDescription>Select a year to import holidays for all subdivisions in
                                germany.</FieldDescription>
                            <FieldGroup>
                                <SelectField fieldName={ "year" } formControl={ form.control } label={ "Holiday Year" }
                                             placeholder={ "Select a year" }
                                             options={ [ ...Array( 11 ).keys() ].map( i => now.getFullYear() - 5 + i ).map( year => (
                                                 {
                                                     label: year.toString(),
                                                     value: year.toString()
                                                 }
                                             ) ) }
                                             disabled={ disabled }>
                                    Required field.
                                </SelectField>
                            </FieldGroup>
                        </FieldSet>
                        <ErrorMessage/>
                        <SuccessMessage
                            message={ `Holidays for year ${ form.getValues( "year" ) } were imported successfully` }/>
                    </FieldGroup>
                </ScrollArea>
                <Separator className={ "my-0" }/>
                <FormActions formId={ formId } label={ "Import" }/>
            </form>
        </FormProvider>
    </FormActionContext.Provider>
}
