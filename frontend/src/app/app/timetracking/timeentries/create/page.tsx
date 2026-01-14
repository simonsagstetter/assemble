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

function CreateTimeentryPage() {
    const { data: user } = useMeSuspense();

    return <FormPageHeader title={ "New" } description={ "Create new time entry" } entity={ "Time Entry" }>
        <TimeEntryUserForm employeeId={ user.employeeId }/>
    </FormPageHeader>
}

export default dynamic( () => Promise.resolve( CreateTimeentryPage ), {
    ssr: false,
} )