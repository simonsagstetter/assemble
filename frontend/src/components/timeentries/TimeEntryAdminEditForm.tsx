/*
 * assemble
 * TimeEntryAdminEditForm.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";

import { FormBuilder } from "@/components/custom-ui/form/form";
import { FieldDescription, FieldGroup, FieldLegend, FieldSet } from "@/components/ui/field";
import { EmployeeLookupField } from "@/components/employees/form/custom-fields";
import { TimeEntryFragment } from "@/components/timeentries/form/fragments";
import { useRouter } from "@bprogress/next/app";
import { useForm } from "react-hook-form";
import { useState } from "react";
import { zodResolver } from "@hookform/resolvers/zod";
import { TimeEntrySchema } from "@/types/timeentries/timeentry.types";
import { parseISO } from "date-fns";
import { isoDurationToMs, msToHHmm } from "@/utils/duration";
import { TimeEntryDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { useTimeEntryCalculations } from "@/components/timeentries/form/utils";
import { useUpdateTimeEntry } from "@/api/rest/generated/query/timeentries/timeentries";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { getUpdateTimeEntryInvalidations } from "@/components/timeentries/form/invalidations";

type TimeEntryAdminEditFormProps = {
    timeentry: TimeEntryDTO
}

export default function TimeEntryAdminEditForm( { timeentry }: Readonly<TimeEntryAdminEditFormProps> ) {
    const router = useRouter();
    const form = useForm( {
        resolver: zodResolver( TimeEntrySchema ),
        defaultValues: {
            projectId: timeentry.project.id,
            employeeId: timeentry.employee.id,
            date: parseISO( String( timeentry.date ) ),
            startTime: "",
            endTime: "",
            totalTime: msToHHmm( isoDurationToMs( timeentry!.totalTime ) ) + ":00",
            pauseTime: msToHHmm( isoDurationToMs( timeentry!.pauseTime ) ) + ":00",
            description: timeentry.description
        }
    } );

    const [ employeeIdIsSelected, setEmployeeIdIsSelected ] = useState<string>( form.getValues( "employeeId" ) );
    const { total } = useTimeEntryCalculations( form );
    const { mutate, isPending, isSuccess, isError } = useUpdateTimeEntry();

    const submitHandler = useFormHandler( {
        mutate,
        form,
        variables: ( data ) => ( {
            id: timeentry.id,
            data
        } ),
        setInvalidations: ( data ) => getUpdateTimeEntryInvalidations( data ),
        onSuccessCallbackAction: () => router.back(),
        setToastSuccessOptions: ( data ) => ( {
            description: "Timeentry was updated",
            action: {
                label: "View",
                onClick: () => router.push( `/app/manage/timeentries/${ data.id }` )
            }
        } )
    } )

    return <FormBuilder form={ form } formId={ "time-entry-admin-edit-form" }
                        status={ {
                            isPending,
                            isSuccess,
                            isError
                        } }
                        submitHandler={ submitHandler }
                        cancelHandler={ () => router.back() }
                        options={ {
                            actionLabel: "Save",
                            successMessage: "Time entry was udpated successfully",
                            maxScrollHeightClassName: employeeIdIsSelected ? "h-[65vh] my-0" : "h-auto my-0",
                        } }>
        <FieldSet>
            <FieldLegend>Employee</FieldLegend>
            <FieldDescription>Select the employee you want to create a time entry for</FieldDescription>
            <FieldGroup>
                <EmployeeLookupField fieldName={ "employeeId" }
                                     formControl={ form.control }
                                     disabled={ form.formState.isSubmitting || isError || isPending || isSuccess }
                                     excludeValues={ [] }
                                     initialSearchTerm={ timeentry.employee.firstname }
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