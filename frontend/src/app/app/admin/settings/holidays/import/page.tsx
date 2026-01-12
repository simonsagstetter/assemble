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
import HolidayImportFrom from "@/components/admin/settings/holidays/HolidayImportFrom";

export default function HolidayImportPage() {
    return <FormPageHeader title={ "Holiday Import" } description={ "Select a year to import holidays" }
                           entity={ "Holiday" }>
        <HolidayImportFrom/>
    </FormPageHeader>
}