/*
 * assemble
 * page.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import ModalHeader from "@/components/custom-ui/ModalHeader";
import TimeEntryAdminCreateForm from "@/components/timeentries/TimeEntryAdminCreateForm";

export default function CreateTimeEntryModal() {
    return <ModalHeader title={ "New" }
                        description={ "Fill out the fields and click new to create a new time entry." }
                        entity={ "Time Entry" }>
        <TimeEntryAdminCreateForm/>
    </ModalHeader>
}