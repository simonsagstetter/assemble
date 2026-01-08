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
import TimeEntryDataTable from "@/components/manage/timeentries/TimeEntryDataTable";
import { useGetAllTimeEntriesSuspense } from "@/api/rest/generated/query/timeentries/timeentries";

function TimeentriesPage() {
    const { data: timeentries } = useGetAllTimeEntriesSuspense();
    return <TimeEntryDataTable timeentries={ timeentries }/>
}

export default dynamic( () => Promise.resolve( TimeentriesPage ), {
    ssr: false,
} );