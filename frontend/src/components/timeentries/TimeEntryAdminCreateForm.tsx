/*
 * assemble
 * TimeEntryAdminCreateForm.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";


import { FormBuilder } from "@/components/custom-ui/form/form";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { TimeEntrySchema } from "@/types/timeentries/timeentry.types";
import { parseISO } from "date-fns";
import { useSearchParams } from "next/navigation";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { useCreateTimeEntry } from "@/api/rest/generated/query/timeentries/timeentries";
import { getCreateTimeEntryInvalidations } from "@/components/timeentries/form/invalidations";
import { useRouter } from "@bprogress/next/app";
import { useTimeEntryCalculations } from "@/components/timeentries/form/utils";
import { FieldDescription, FieldGroup, FieldLegend, FieldSet } from "@/components/ui/field";
import { EmployeeLookupField } from "@/components/employees/form/custom-fields";
import { TimeEntryFragment } from "@/components/timeentries/form/fragments";

export default function TimeEntryAdminCreateForm() {
    const router = useRouter();

    const searchParams = useSearchParams()
    const dateParam = searchParams.get( "date" ) ?? "";

    const form = useForm( {
        resolver: zodResolver( TimeEntrySchema ),
        defaultValues: {
            projectId: "",
            employeeId: "",
            date: dateParam ? parseISO( dateParam ) : new Date(),
            startTime: "",
            endTime: "",
            totalTime: "00:00:00",
            pauseTime: "00:00:00",
            description: ""
        }
    } );

    const [ employeeIdIsSelected, setEmployeeIdIsSelected ] = useState<string>( form.getValues( "employeeId" ) );
    const { total } = useTimeEntryCalculations( form );
    const { mutate, isPending, isSuccess, isError } = useCreateTimeEntry();

    const submithandler = useFormHandler( {
        mutate,
        form,
        variables: ( data ) => ( {
            data
        } ),
        setInvalidations: ( data ) => getCreateTimeEntryInvalidations( data ),
        onSuccessCallbackAction: () => router.back(),
        setToastSuccessOptions: ( data ) => ( {
            description: "Timeentry was created",
            action: {
                label: "View",
                onClick: () => router.push( `/app/manage/timeentries/${ data.id }` )
            }
        } )
    } );

    return <FormBuilder form={ form }
                        formId={ "time-entry-admin-create-form" }
                        status={ {
                            isPending,
                            isSuccess,
                            isError,
                        } }
                        submitHandler={ submithandler }
                        cancelHandler={ () => router.back() }
                        options={ {
                            maxScrollHeightClassName: employeeIdIsSelected ? "h-[65vh] my-0" : "h-auto my-0",
                            successMessage: "Time entry was created successfully",
                            disabledOnSuccess: true,
                            actionLabel: "New"
                        } }
    >
        <FieldSet>
            <FieldLegend>Employee</FieldLegend>
            <FieldDescription>Select the employee you want to create a time entry for</FieldDescription>
            <FieldGroup>
                <EmployeeLookupField fieldName={ "employeeId" }
                                     formControl={ form.control }
                                     disabled={ form.formState.isSubmitting || isError || isPending || isSuccess }
                                     excludeValues={ [] }
                                     onSelectHandler={ setEmployeeIdIsSelected }
                />

            </FieldGroup>
        </FieldSet>
        { employeeIdIsSelected ?
            <TimeEntryFragment total={ total }
                               employeeId={ form.getValues( "employeeId" ) }
                               isRestrictedSearch={ false }
            />
            : null }
    </FormBuilder>
}