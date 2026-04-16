/*
 * assemble
 * page.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import FormPageHeader from "@/components/custom-ui/FormPageHeader";
import TimeEntryAdminCreateForm from "@/components/timeentries/TimeEntryAdminCreateForm";

export default function CreateTimeEntryPage() {
    return <FormPageHeader title={ "New" }
                           description={ "Fill out the fields and click new to create a new time entry." }
                           entity={ "Time Entry" }>
        <TimeEntryAdminCreateForm/>
    </FormPageHeader>
}