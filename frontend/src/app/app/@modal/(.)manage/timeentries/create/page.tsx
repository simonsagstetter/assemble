/*
 * assemble
 * page.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import TimeEntryAdminForm from "@/components/manage/timeentries/TimeEntryAdminForm";
import ModalHeader from "@/components/custom-ui/ModalHeader";

export default function CreateTimeEntryModal() {
    return <ModalHeader title={ "New" }
                        description={ "Fill out the fields and click new to create a new time entry." }
                        entity={ "Time Entry" }>
        <TimeEntryAdminForm/>
    </ModalHeader>
}