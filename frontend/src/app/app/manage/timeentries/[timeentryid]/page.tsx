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

import dynamic from "next/dynamic";
import { useParams } from "next/navigation";
import { useGetTimeEntryByIdSuspense } from "@/api/rest/generated/query/timeentries/timeentries";
import TimeEntryDetail from "@/components/manage/timeentries/TimeEntryDetail";

function TimeEntryDetailsPage() {
    const { timeentryid } = useParams<{ timeentryid: string }>();
    const { data: timeEntry } = useGetTimeEntryByIdSuspense( timeentryid );

    return <TimeEntryDetail timeEntry={ timeEntry }/>
}

export default dynamic( () => Promise.resolve( TimeEntryDetailsPage ), {
    ssr: false,
} )