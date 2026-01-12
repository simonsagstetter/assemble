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
import HolidayChooseYearForm from "@/components/admin/settings/holidays/HolidayChooseYearForm";
import dynamic from "next/dynamic";
import { useGetImportedYearsSuspense } from "@/api/rest/generated/query/holiday-import/holiday-import";

function ChooseHolidayYearPage() {
    const { data: years } = useGetImportedYearsSuspense();
    return <FormPageHeader title={ "Choose a Year" } description={ "Select a year to view holidays" }
                           entity={ "Holiday" }>
        <HolidayChooseYearForm years={ years }/>
    </FormPageHeader>
}

export default dynamic( () => Promise.resolve( ChooseHolidayYearPage ), { ssr: false } );