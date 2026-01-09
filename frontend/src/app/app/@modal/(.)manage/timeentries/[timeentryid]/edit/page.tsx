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
import TimeEntryAdminForm from "@/components/manage/timeentries/TimeEntryAdminForm";
import dynamic from "next/dynamic";
import ModalHeader from "@/components/custom-ui/ModalHeader";

function EditTimeEntryModal() {
    const { timeentryid } = useParams<{ timeentryid: string }>();
    const { data: timeEntry } = useGetTimeEntryByIdSuspense( timeentryid );

    return <ModalHeader title={ "Edit" } description={ "Update the fields and click save to update the time entry." }
                        entity={ "Time Entry" }>

        <TimeEntryAdminForm timeentry={ timeEntry }/>
    </ModalHeader>
}

export default dynamic( () => Promise.resolve( EditTimeEntryModal ), {
    ssr: false,
} )