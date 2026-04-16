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
import { useGetOwnTimeEntryByIdSuspense } from "@/api/rest/generated/query/timeentries/timeentries";
import TimeEntryEditForm from "@/components/timeentries/TimeEntryEditForm";


function UpdateTimeEntryPage() {
    const { id } = useParams<{ id: string }>();
    const { data: timeEntry } = useGetOwnTimeEntryByIdSuspense( id );

    return <FormPageHeader title={ "Edit" }
                           description={ "Edit the fields and click save to update the time entry." }
                           entity={ "Time Entry" }>
        <TimeEntryEditForm timeentry={ timeEntry }/>
    </FormPageHeader>
}

export default dynamic( () => Promise.resolve( UpdateTimeEntryPage ), {
    ssr: false,
} )