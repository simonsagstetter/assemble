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

import { useParams } from "next/navigation";
import { useGetOwnTimeEntryByIdSuspense } from "@/api/rest/generated/query/timeentries/timeentries";
import dynamic from "next/dynamic";
import TimeEntryDeleteForm from "@/components/timetracking/timeentries/TimeEntryDeleteForm";
import FormPageHeader from "@/components/custom-ui/FormPageHeader";

function DeleteTimeEntryPage() {
    const { id } = useParams<{ id: string }>();
    const { data: timeEntry } = useGetOwnTimeEntryByIdSuspense( id );
    return <FormPageHeader title={ "Delete" } description={ "Confirm the action below to delete this time entry." }
                           entity={ "Time Entry" }>
        <TimeEntryDeleteForm timeentry={ timeEntry }/>
    </FormPageHeader>
}

export default dynamic( () => Promise.resolve( DeleteTimeEntryPage ), {
    ssr: false,
} )