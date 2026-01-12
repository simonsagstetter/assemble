/*
 * assemble
 * Calendar.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import MonthView from "@/components/custom-ui/calendar/MonthView";
import { HolidayDTO, TimeEntryDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import useCalendar from "@/hooks/useCalendar";
import { useEffect } from "react";
import CalenderLoading from "@/components/custom-ui/calendar/CalendarLoading";

type CalendarProps = {
    events: TimeEntryDTO[],
    holidays: HolidayDTO[],
    subdivisionCode: string;
}

export default function Calendar( { events, holidays, subdivisionCode }: CalendarProps ) {
    const { setEvents, setHolidays, setSettings, settings, isLoading } = useCalendar();

    useEffect( () => {
        const eventData = Object
            .groupBy( events, event => event.date );

        const holidayData = Object
            .groupBy( holidays.filter( h => h.startDate ), holiday => holiday.startDate! );

        setEvents( eventData );
        setHolidays( holidayData );
        setSettings( {
            newLink: "/app/timetracking/timeentries/create",
            view: "month",
            subdivisionCode
        } );
    }, [ events, setEvents, setSettings, holidays, setHolidays, subdivisionCode ] );


    return <>
        { settings.view === "month" ? <MonthView/> : null }
        { isLoading ? <CalenderLoading/> : null }
    </>
}