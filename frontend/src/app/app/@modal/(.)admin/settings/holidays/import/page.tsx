/*
 * assemble
 * page.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import HolidayImportFrom from "@/components/admin/settings/holidays/HolidayImportFrom";
import ModalHeader from "@/components/custom-ui/ModalHeader";

export default function HolidayImportPage() {
    return <ModalHeader title={ "Holiday Import" } description={ "Select a year to import holidays" }
                        entity={ "Holiday" }>
        <HolidayImportFrom/>
    </ModalHeader>
}