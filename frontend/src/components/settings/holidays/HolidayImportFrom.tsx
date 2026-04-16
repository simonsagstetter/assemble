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

import { useForm } from "react-hook-form";
import { useGetHolidaysFromYear } from "@/api/rest/generated/query/holiday-import/holiday-import";
import { useRouter } from "@bprogress/next/app";
import { FieldDescription, FieldGroup, FieldLegend, FieldSet } from "@/components/ui/field";
import { zodResolver } from "@hookform/resolvers/zod";
import { HolidaySchema } from "@/types/holidays/holiday.types";
import { format } from "date-fns";
import { SelectField } from "@/components/custom-ui/form/fields";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { getImportHolidaysInvalidations } from "@/components/settings/form/invalidations";
import { FormBuilder } from "@/components/custom-ui/form/form";

export default function HolidayImportFrom() {
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

    const handleSubmit = useFormHandler( {
        form,
        mutate,
        variables: ( data ) => ( {
            year: data.year
        } ),
        setInvalidations: () => getImportHolidaysInvalidations( form.getValues( "year" ) ),
        onSuccessCallbackAction: () => handleCancel(),
        onErrorCallbackAction: () => {
            form.setError( "root", {
                type: "manual",
                message: "Could not import holidays. Please try again later. If the problem persists, please contact the administrator."
            } );
        },
        setToastSuccessOptions: () => ( {
            description: "Holidays were imported"
        } )
    } )


    const handleCancel = () => {
        router.push( `/app/admin/settings/holidays?year=${ form.getValues( "year" ) }` );
    }

    return <FormBuilder form={ form }
                        formId={ formId }
                        submitHandler={ handleSubmit }
                        cancelHandler={ handleCancel }
                        status={ {
                            isPending,
                            isError,
                            isSuccess
                        } }
                        options={ {
                            actionLabel: "Import",
                            successMessage: `Holidays for year ${ form.getValues( "year" ) } were imported successfully`,
                        } }
    >
        <FieldSet>
            <FieldLegend>Import Details</FieldLegend>
            <FieldDescription>Select a year to import holidays for all subdivisions in
                germany.</FieldDescription>
            <FieldGroup>
                <SelectField fieldName={ "year" } formControl={ form.control } label={ "Holiday Year" }
                             placeholder={ "Select a year" }
                             options={ [ ...new Array( 11 ).keys() ].map( i => now.getFullYear() - 5 + i ).map( year => (
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
    </FormBuilder>
}
