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

function CalendarPage() {
    const { data: events } = useGetOwnTimeEntriesSuspense( {
        aroundDate: format( new Date(), "yyyy-MM-dd" ),
    } );
    return <CalendarProvider>
        <Calendar events={ events }/>
    </CalendarProvider>
}

export default dynamic( () => Promise.resolve( CalendarPage ), {
    ssr: false,
} )