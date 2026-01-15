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

import ModalHeader from "@/components/custom-ui/ModalHeader";
import dynamic from "next/dynamic";
import { useMeSuspense } from "@/api/rest/generated/query/users/users";
import TimeEntryUserForm from "@/components/timetracking/timeentries/TimeEntryUserForm";
import { useSearchParams } from "next/navigation";
import { format } from "date-fns";
import { useGetOwnTimeEntriesSuspense } from "@/api/rest/generated/query/timeentries/timeentries";

function CreateTimeEntryModal() {
    const searchParams = useSearchParams();
    const date = searchParams.get( "date" ) || format( new Date(), "yyyy-MM-dd" );
    const { data: user } = useMeSuspense();
    const { data: relatedTimeEntries } = useGetOwnTimeEntriesSuspense( {
        exactDate: date
    } );
    return <ModalHeader title={ "New" } description={ "Create a new time entry." } entity={ "Time Entry" }>
        <TimeEntryUserForm employeeId={ user.employeeId } relatedTimeEntries={ relatedTimeEntries }/>
    </ModalHeader>
}

export default dynamic( () => Promise.resolve( CreateTimeEntryModal ), {
    ssr: false,
} )