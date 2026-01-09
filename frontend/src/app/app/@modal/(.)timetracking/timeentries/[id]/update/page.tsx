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
import TimeEntryUserForm from "@/components/timetracking/timeentries/TimeEntryUserForm";
import dynamic from "next/dynamic";
import ModalHeader from "@/components/custom-ui/ModalHeader";

function UpdateTimeEntryModal() {
    const { id } = useParams<{ id: string }>();
    const { data: timeEntry } = useGetOwnTimeEntryByIdSuspense( id );
    return <ModalHeader title={ "Update" }
                        description={ "Update the fields and click save to update the time entry." }
                        entity={ "Time Entry" }>
        <TimeEntryUserForm timeentry={ timeEntry }/>
    </ModalHeader>
}

export default dynamic( () => Promise.resolve( UpdateTimeEntryModal ), {
    ssr: false,
} )