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
import { useMeSuspense } from "@/api/rest/generated/query/users/users";
import TimeEntryUserForm from "@/components/timetracking/timeentries/TimeEntryUserForm";
import { useSearchParams } from "next/navigation";
import { format } from "date-fns";
import { useGetOwnTimeEntriesSuspense } from "@/api/rest/generated/query/timeentries/timeentries";

function CreateTimeentryPage() {
    const searchParams = useSearchParams();
    const date = searchParams.get( "date" ) || format( new Date(), "yyyy-MM-dd" );
    const { data: user } = useMeSuspense();
    const { data: relatedTimeEntries } = useGetOwnTimeEntriesSuspense( {
        exactDate: date
    } );

    return <FormPageHeader title={ "New" } description={ "Create new time entry" } entity={ "Time Entry" }>
        <TimeEntryUserForm employeeId={ user.employeeId } relatedTimeEntries={ relatedTimeEntries }/>
    </FormPageHeader>
}

export default dynamic( () => Promise.resolve( CreateTimeentryPage ), {
    ssr: false,
} )