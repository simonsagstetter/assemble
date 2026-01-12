/*
 * assemble
 * HolidayChooseYearForm.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";

import { ScrollArea } from "@/components/ui/scroll-area";
import { FieldGroup, FieldSet } from "@/components/ui/field";
import { SelectField } from "@/components/custom-ui/form/fields";
import { ErrorMessage, SuccessMessage } from "@/components/custom-ui/form/messages";
import { Separator } from "@/components/ui/separator";
import { FormActions } from "@/components/custom-ui/form/actions";
import { FormProvider, useForm } from "react-hook-form";
import { format } from "date-fns";
import useModalContext from "@/hooks/useModalContext";
import { FormActionContext } from "@/store/formActionStore";
import { useRouter } from "@bprogress/next/app";
import { zodResolver } from "@hookform/resolvers/zod";
import { HolidayChooseYearFormData, HolidayChooseYearSchema } from "@/types/holidays/holiday.types";

type HolidayChooseYearFormProps = {
    years: number[]
}

export default function HolidayChooseYearForm( { years }: HolidayChooseYearFormProps ) {
    const modalContext = useModalContext();
    const router = useRouter();

    const currentYear = new Date().getFullYear();
    const formId = "holiday-import-from-form";
    const form = useForm( {
        resolver: zodResolver( HolidayChooseYearSchema ),
        defaultValues: {
            year: years.includes( currentYear ) ? currentYear.toString() : "",
        }
    } )

    const handleChooseYear = ( data: HolidayChooseYearFormData ) => {
        setTimeout( () => router.push( `/app/admin/settings/holidays?year=${ data.year }` ), 100 );
        router.back();
    }

    const handleCancel = () => {
        if ( modalContext ) {
            modalContext.setOpen( false );
        }
        router.back();
    }

    return <FormActionContext.Provider value={ {
        isPending: false, isSuccess: false, isError: false, disableOnSuccess: true, handleCancel
    } }>
        <FormProvider { ...form }>
            <form id={ formId } onSubmit={ form.handleSubmit( handleChooseYear ) } className="space-y-8">
                <ScrollArea className={ `${ modalContext ? "h-auto my-0" : "" }` }>
                    <FieldGroup className={ "py-4 px-8" }>
                        <FieldSet>
                            <FieldGroup>
                                <SelectField fieldName={ "year" } formControl={ form.control } label={ "Holiday Year" }
                                             placeholder={ "Select a year" }
                                             options={ years.map( year => ( {
                                                 label: year.toString(),
                                                 value: year.toString()
                                             } ) ) } disabled={ false }>
                                    Choose a year from the list. Only years which are already imported will be shown.
                                </SelectField>
                            </FieldGroup>
                        </FieldSet>
                        <ErrorMessage/>
                        <SuccessMessage
                            message={ `Holidays for year ${ form.getValues( "year" ) } were imported successfully` }/>
                    </FieldGroup>
                </ScrollArea>
                <Separator className={ "my-0" }/>
                <FormActions formId={ formId } label={ "Choose" } hideCancel/>
            </form>
        </FormProvider>
    </FormActionContext.Provider>
}