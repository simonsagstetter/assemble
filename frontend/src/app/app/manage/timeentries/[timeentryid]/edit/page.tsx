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
import TimeEntryAdminEditForm from "@/components/timeentries/TimeEntryAdminEditForm";

function EditTimeEntryPage() {
    const { timeentryid } = useParams<{ timeentryid: string }>();
    const { data: timeEntry } = useGetTimeEntryByIdSuspense( timeentryid );

    return <FormPageHeader title={ "Edit" } description={ "Update the fields and click save to edit the time entry." }
                           entity={ "Time Entry" }>

        <TimeEntryAdminEditForm timeentry={ timeEntry }/>
    </FormPageHeader>
}

export default dynamic( () => Promise.resolve( EditTimeEntryPage ), {
    ssr: false,
} )