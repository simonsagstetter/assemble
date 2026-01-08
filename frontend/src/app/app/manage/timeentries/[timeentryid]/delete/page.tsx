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
import { useGetTimeEntryByIdSuspense } from "@/api/rest/generated/query/timeentries/timeentries";
import FormPageHeader from "@/components/custom-ui/FormPageHeader";
import dynamic from "next/dynamic";
import TimeEntryAdminDeleteForm from "@/components/manage/timeentries/TimeEntryAdminDeleteForm";

function DeleteTimeEntryPage() {
    const { timeentryid } = useParams<{ timeentryid: string }>();
    const { data: timeEntry } = useGetTimeEntryByIdSuspense( timeentryid );

    return <FormPageHeader title={ "Delete" } description={ "Confirm the action below to delete this time entrry." }
                           entity={ "Time Entry" }>
        <TimeEntryAdminDeleteForm timeentry={ timeEntry }/>
    </FormPageHeader>
}

export default dynamic( () => Promise.resolve( DeleteTimeEntryPage ), {
    ssr: false,
} )