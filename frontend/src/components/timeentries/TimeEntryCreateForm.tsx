/*
 * assemble
 * TimeEntryCreateForm.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { TimeEntrySchema } from "@/types/timeentries/timeentry.types";
import { useSearchParams } from "next/navigation";
import { parseISO } from "date-fns";
import { useRelatedTimeEntries, useTimeEntryCalculations } from "@/components/timeentries/form/utils";
import { FormBuilder } from "@/components/custom-ui/form/form";
import { TimeEntryFragment } from "@/components/timeentries/form/fragments";
import { useRouter } from "@bprogress/next/app";
import { useCreateOwnTimeEntry } from "@/api/rest/generated/query/timeentries/timeentries";
import useFormHandler from "@/components/custom-ui/form/handlers";
import { getCreateTimeEntryInvalidations } from "@/components/timeentries/form/invalidations";
import useUserContext from "@/hooks/use-user";

type TimeEntryCreateFormProps = {
    employeeId: string;
}

export default function TimeEntryCreateForm( { employeeId, }: Readonly<TimeEntryCreateFormProps> ) {
    const router = useRouter();

    const { isManager, isAdmin, isSuperUser } = useUserContext();
    const hasPriviledge = isManager || isAdmin || isSuperUser;

    const searchParams = useSearchParams()
    const dateParam = searchParams.get( "date" ) ?? "";

    const form = useForm( {
        resolver: zodResolver( TimeEntrySchema ),
        defaultValues: {
            projectId: "",
            employeeId: employeeId,
            date: dateParam ? parseISO( dateParam ) : new Date(),
            startTime: "",
            endTime: "",
            totalTime: "00:00:00",
            pauseTime: "00:00:00",
            description: ""
        }
    } );

    const { total } = useTimeEntryCalculations( form );
    const { data: relatedTimeEntries } = useRelatedTimeEntries( form );
    const { mutate, isPending, isError, isSuccess } = useCreateOwnTimeEntry();

    const submitHandler = useFormHandler( {
        mutate,
        form,
        variables: ( data ) => ( {
            data
        } ),
        setInvalidations: ( data ) => getCreateTimeEntryInvalidations( data ),
        onSuccessCallbackAction: () => router.back(),
        setToastSuccessOptions: ( data ) => ( {
            description: "Timeentry created",
            action: hasPriviledge ? {
                label: "View",
                onClick: () => router.push( `/app/manage/timeentries/${ data.id }` )
            } : null
        } )
    } )


    return <FormBuilder form={ form } formId={ "time-entry-create-form" }
                        status={ { isPending, isError, isSuccess } }
                        submitHandler={ submitHandler }
                        cancelHandler={ () => router.back() }
                        options={ {
                            maxScrollHeightClassName: "h-[65vh]",
                            actionLabel: "New",
                            successMessage: "Time entry was created successfully",
                            disabledOnSuccess: true
                        } }
    >
        <TimeEntryFragment employeeId={ employeeId }
                           total={ total } relatedTimeEntries={ relatedTimeEntries }
        />
    </FormBuilder>
}