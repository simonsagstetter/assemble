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

import { FieldGroup, FieldSet } from "@/components/ui/field";
import { SelectField } from "@/components/custom-ui/form/fields";
import { useForm } from "react-hook-form";
import { useRouter } from "@bprogress/next/app";
import { zodResolver } from "@hookform/resolvers/zod";
import { HolidayChooseYearFormData, HolidayChooseYearSchema } from "@/types/holidays/holiday.types";
import { FormBuilder } from "@/components/custom-ui/form/form";

type HolidayChooseYearFormProps = {
    years: number[]
}

export default function HolidayChooseYearForm( { years }: Readonly<HolidayChooseYearFormProps> ) {
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
        router.push( `/app/admin/settings/holidays?year=${ data.year }` );
    }

    const handleCancel = () => {
        router.back();
    }

    return <FormBuilder form={ form }
                        formId={ formId }
                        status={ {
                            isSuccess: false,
                            isError: false,
                            isPending: false,
                        } }
                        submitHandler={ handleChooseYear }
                        cancelHandler={ handleCancel }
                        options={ {
                            disabledOnSuccess: true,
                            successMessage: `Holidays for year ${ form.getValues( "year" ) } were imported successfully`,
                            maxScrollHeightClassName: "h-auto my-0",
                            actionLabel: "Cooose",
                            actionHideCancel: true,
                        } }
    >
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
    </FormBuilder>
}