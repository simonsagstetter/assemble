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

import dynamic from "next/dynamic";
import { useGetHolidaysByYearAndSubdivisionCodeSuspense } from "@/api/rest/generated/query/holidays/holidays";
import { useGetAppSettingsSuspense } from "@/api/rest/generated/query/app-settings/app-settings";
import { format } from "date-fns";
import HolidayDataTable from "@/components/admin/settings/holidays/HolidayDataTable";
import { useSearchParams } from "next/navigation";

function HolidaysPage() {
    const searchParams = useSearchParams();
    const year = searchParams.get( "year" ) || format( new Date(), "yyyy" );
    const { data: settings } = useGetAppSettingsSuspense();
    const { data: holidays } = useGetHolidaysByYearAndSubdivisionCodeSuspense( {
        subdivisionCode: "DE-" + settings.holidaySubdivisionCode,
        year
    } )

    return <HolidayDataTable holidays={ holidays } year={ year }/>
}

export default dynamic( () => Promise.resolve( HolidaysPage ), { ssr: false } );