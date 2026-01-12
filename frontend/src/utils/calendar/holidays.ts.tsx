/*
 * assemble
 * holidays.ts.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { HolidaysByDate } from "@/store/calendar-store";
import { EventData } from "@/types/calendar/calendar.types";
import { format } from "date-fns";

function createHolidayEvents(
    date: Date,
    holidays: HolidaysByDate
): EventData[] {
    const dateKey = format( date, "yyyy-MM-dd" );
    const thisHolidays = holidays[ dateKey ] || [];
    return thisHolidays.map( holiday => ( {
        id: holiday.id,
        updateLink: "",
        deleteLink: "",
        title: holiday.name,
        time: 0,
        isMultiDay: false,
        color: "gray"
    } satisfies EventData ) );
}

export { createHolidayEvents };
