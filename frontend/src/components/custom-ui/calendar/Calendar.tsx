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
import { TimeEntryDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import useCalendar from "@/hooks/useCalendar";
import { useEffect } from "react";

export default function Calendar( { events }: { events: TimeEntryDTO[] } ) {
    const { setEvents, setSettings, settings } = useCalendar();

    useEffect( () => {
        const eventData = Object
            .groupBy( events, event => event.date );

        setEvents( eventData );
        setSettings( {
            newLink: "/app/timetracking/timeentries/create",
            view: "month"
        } );
    }, [ events, setEvents, setSettings ] );

    if ( settings.view === "month" ) return <MonthView/>;

    return <></>
}