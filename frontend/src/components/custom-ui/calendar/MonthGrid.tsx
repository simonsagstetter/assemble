/*
 * assemble
 * MonthGrid.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
import Day from "@/components/custom-ui/calendar/Day";
import {
    addDays,
    subDays,
    subMonths
} from "date-fns";
import useCalendar from "@/hooks/useCalendar";
import {
    calculateWeekTotals,
    createDayInfo,
    getMonthDayRange
} from "@/utils/calendar/timeentries";
import { ReactElement } from "react";
import { MonthDayRange } from "@/types/calendar/calendar.types";
import { HolidaysByDate, TimeEntriesByDate } from "@/store/calendar-store";
import { createHolidayEvents } from "@/utils/calendar/holidays";

const rowClasses = {
    4: "grid-rows-4",
    5: "grid-rows-5",
    6: "grid-rows-6"
}

export default function MonthGrid() {
    const { currentDate, events, selectedDate, holidays } = useCalendar();

    const range = getMonthDayRange( currentDate );
    const days: ReactElement[] = [];

    const rangeStart = subDays( range.firstDay, range.firstDayWeekDay );
    const rangeEnd = addDays( range.lastDay, range.nextMonthDays );
    const weekTotals = calculateWeekTotals( events, rangeStart, rangeEnd );

    days.push( ...renderPreviousMonthDays( range, events, weekTotals ) );
    days.push( ...renderCurrentMonthDays( range, events, holidays, weekTotals, selectedDate ) );
    days.push( ...renderNextMonthDays( range, events, weekTotals ) );

    const rows = Math.ceil( days.length / 7 );
    const cssClasses = `grid flex-1 ${ rowClasses[ rows as keyof typeof rowClasses ] } grid-cols-7`;

    return <div className={ cssClasses }>
        { days }
    </div>

}

function renderPreviousMonthDays(
    range: MonthDayRange,
    events: TimeEntriesByDate,
    weekTotals: Map<string, number>
): ReactElement[] {
    const days: ReactElement[] = [];
    const prevMonth = subMonths( range.firstDay, 1 );

    for ( let i = range.firstDayWeekDay - 1; i >= 0; i-- ) {
        const day = range.prevMonthDays - i;
        const date = new Date( prevMonth.getFullYear(), prevMonth.getMonth(), day );
        const dayInfo = createDayInfo( date, events, weekTotals, { isReadOnly: true } );

        days.push( <Day { ...dayInfo } key={ `prev-${ dayInfo.key }` }/> );
    }

    return days;
}

function renderCurrentMonthDays(
    range: MonthDayRange,
    events: TimeEntriesByDate,
    holidays: HolidaysByDate,
    weekTotals: Map<string, number>,
    selectedDate: Date
): ReactElement[] {
    const days: ReactElement[] = [];

    for ( let day = 1; day <= range.daysInMonth; day++ ) {
        const date = addDays( range.firstDay, day - 1 );
        const dayInfo = createDayInfo( date, events, weekTotals, { selectedDate } );
        const holidayEvents = createHolidayEvents( date, holidays );
        if ( holidayEvents.length !== 0 ) dayInfo.isHoliday = true;
        dayInfo.dayEvents.push( ...holidayEvents );

        days.push( <Day { ...dayInfo } key={ `current-${ dayInfo.key }` }/> );
    }

    return days;
}

function renderNextMonthDays(
    range: MonthDayRange,
    events: TimeEntriesByDate,
    weekTotals: Map<string, number>
): ReactElement[] {
    const days: ReactElement[] = [];

    for ( let i = 0; i < range.nextMonthDays; i++ ) {
        const date = addDays( range.lastDay, i + 1 );
        const dayInfo = createDayInfo( date, events, weekTotals, { isReadOnly: true } );

        days.push( <Day { ...dayInfo } key={ `next-${ dayInfo.key }` }/> );
    }

    return days;
}