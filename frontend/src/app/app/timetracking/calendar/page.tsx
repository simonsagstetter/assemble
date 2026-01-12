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
import Calendar from "@/components/custom-ui/calendar/Calendar";
import dynamic from "next/dynamic";
import {
    useGetOwnTimeEntriesSuspense
} from "@/api/rest/generated/query/timeentries/timeentries";
import CalendarProvider from "@/store/calendar-store";
import { format } from "date-fns";
import {
    useGetCompanySubdivisionCodeSuspense,
    useGetHolidaysByYearAndSubdivisionCodeSuspense
} from "@/api/rest/generated/query/holidays/holidays";

function CalendarPage() {
    const { data: subdivionCode } = useGetCompanySubdivisionCodeSuspense();
    const { data: holidays } = useGetHolidaysByYearAndSubdivisionCodeSuspense( {
        year: format( new Date(), "yyyy" ),
        subdivisionCode: "DE-" + subdivionCode
    } );
    const { data: events } = useGetOwnTimeEntriesSuspense( {
        aroundDate: format( new Date(), "yyyy-MM-dd" ),
    } );


    return <CalendarProvider>
        <Calendar events={ events } holidays={ holidays } subdivisionCode={ subdivionCode }/>
    </CalendarProvider>
}

export default dynamic( () => Promise.resolve( CalendarPage ), {
    ssr: false,
} )