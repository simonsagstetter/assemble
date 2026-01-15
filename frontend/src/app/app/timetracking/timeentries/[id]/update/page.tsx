/*
 * assemble
 * page.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

"use client";

import FormPageHeader from "@/components/custom-ui/FormPageHeader";
import dynamic from "next/dynamic";
import { useParams } from "next/navigation";
import {
    useGetOwnTimeEntriesSuspense,
    useGetOwnTimeEntryByIdSuspense
} from "@/api/rest/generated/query/timeentries/timeentries";
import TimeEntryUserForm from "@/components/timetracking/timeentries/TimeEntryUserForm";


function UpdateTimeEntryPage() {
    const { id } = useParams<{ id: string }>();
    const { data: timeEntry } = useGetOwnTimeEntryByIdSuspense( id );
    const { data: relatedTimeEntries } = useGetOwnTimeEntriesSuspense( {
        exactDate: timeEntry.date
    } );
    return <FormPageHeader title={ "Update" }
                           description={ "Update the fields and click save to update the time entry." }
                           entity={ "Time Entry" }>
        <TimeEntryUserForm timeentry={ timeEntry } relatedTimeEntries={ relatedTimeEntries }/>
    </FormPageHeader>
}

export default dynamic( () => Promise.resolve( UpdateTimeEntryPage ), {
    ssr: false,
} )