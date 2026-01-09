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

function CreateTimeEntryModal() {
    const { data: user } = useMeSuspense();
    return <ModalHeader title={ "New" } description={ "Create a new time entry." } entity={ "Time Entry" }>
        <TimeEntryUserForm employeeId={ user.employeeId }/>
    </ModalHeader>
}

export default dynamic( () => Promise.resolve( CreateTimeEntryModal ), {
    ssr: false,
} )