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
import ModalHeader from "@/components/custom-ui/ModalHeader";
import TimeEntryDeleteForm from "@/components/timetracking/timeentries/TimeEntryDeleteForm";

function DeleteTimeEntryModal() {
    const { id } = useParams<{ id: string }>();
    const { data: timeEntry } = useGetOwnTimeEntryByIdSuspense( id );
    return <ModalHeader title={ "Delete" } description={ "Confirm the action below to delete this time entry." }
                        entity={ "Time Entry" }>
        <TimeEntryDeleteForm timeentry={ timeEntry }/>
    </ModalHeader>
}

export default dynamic( () => Promise.resolve( DeleteTimeEntryModal ), {
    ssr: false,
} )