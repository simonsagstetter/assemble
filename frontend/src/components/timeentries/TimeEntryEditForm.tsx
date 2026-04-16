/*
 * assemble
 * TimeEntryEditForm.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";

import { useRouter } from "@bprogress/next/app";
import { TimeEntryDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { FormBuilder } from "@/components/custom-ui/form/form";
import { TimeEntryFragment } from "@/components/timeentries/form/fragments";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { TimeEntrySchema } from "@/types/timeentries/timeentry.types";
import { parseISO } from "date-fns";
import { isoDurationToMs, msToHHmm } from "@/utils/duration";
import { useRelatedTimeEntries, useTimeEntryCalculations } from "@/components/timeentries/form/utils";
import { useUpdateOwnTimeEntry } from "@/api/rest/generated/query/timeentries/timeentries";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { getUpdateTimeEntryInvalidations } from "@/components/timeentries/form/invalidations";
import useUserContext from "@/hooks/use-user";

type TimeEntryEditFormProps = {
    timeentry: TimeEntryDTO,
}

export default function TimeEntryEditForm( { timeentry }: Readonly<TimeEntryEditFormProps>
) {
    const router = useRouter();

    const { isManager, isAdmin, isSuperUser } = useUserContext();
    const hasPriviledge = isManager || isAdmin || isSuperUser;

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

    const { total } = useTimeEntryCalculations( form );
    const { data } = useRelatedTimeEntries( form );
    const relatedTimeEntries = data?.filter( entry => entry.id !== timeentry.id );
    const { mutate, isError, isSuccess, isPending } = useUpdateOwnTimeEntry();

    const submithandler = useFormHandler( {
        mutate,
        form,
        variables: ( data ) => ( {
            id: timeentry.id,
            data: data
        } ),
        setInvalidations: ( data ) => getUpdateTimeEntryInvalidations( data ),
        onSuccessCallbackAction: () => router.back(),
        setToastSuccessOptions: ( data ) => ( {
            description: "Timeentry was updated",
            action: hasPriviledge ? {
                label: "View",
                onClick: () => router.push( `/app/manage/timeentries/${ data.id }` )
            } : null
        } )
    } );

    return <FormBuilder form={ form } formId={ "time-entry-edit-form" }
                        status={ {
                            isSuccess,
                            isPending,
                            isError,
                        } }
                        submitHandler={ submithandler }
                        cancelHandler={ () => router.back() }
                        options={ {
                            actionLabel: "Save",
                            maxScrollHeightClassName: "h-[65vh] my-0",
                            successMessage: "Time entry was updated successfully",
                        } }
    >
        <TimeEntryFragment employeeId={ timeentry.employee.id }
                           total={ total }
                           relatedTimeEntries={ relatedTimeEntries }
        />
    </FormBuilder>
}