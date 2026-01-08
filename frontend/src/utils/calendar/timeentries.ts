/*
 * assemble
 * timeentries.ts
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { TimeEntryDTO } from "@/api/rest/generated/query/openAPIDefinition.schemas";
import { isoDurationToMs } from "@/utils/duration";
import { DayInfo, EventData, MonthDayRange } from "@/types/calendar/calendar.types";
import {
    addDays,
    format,
    getDay,
    getDaysInMonth, isSameDay,
    lastDayOfMonth,
    startOfMonth,
    startOfWeek,
    subMonths
} from "date-fns";
import { getDayMondayBased } from "@/utils/datetime";
import { TimeEntriesByDate } from "@/store/calendar-store";
import { colorClasses } from "@/config/calendar/calendar.config";

function calculateDayTotal( timeentries: TimeEntryDTO[] ): number {
    return timeentries.reduce( ( sum, entry ) => {
        return sum + isoDurationToMs( entry.totalTime ) - isoDurationToMs( entry.pauseTime );
    }, 0 );
}

function mapTimeEntriesToDayEvents( timeentries: TimeEntryDTO[] ): EventData[] {
    return timeentries.map( entry => ( {
        id: entry.id,
        updateLink: `/app/timetracking/timeentries/${ entry.id }/update`,
        deleteLink: `/app/timetracking/timeentries/${ entry.id }/delete`,
        title: entry.project.name,
        time: isoDurationToMs( entry.totalTime ) - isoDurationToMs( entry.pauseTime ),
        isMultiDay: false,
        color: entry.project.color.toLowerCase() as keyof typeof colorClasses,
        tooltip: entry.description
    } satisfies EventData ) );
}

function calculateWeekTotals(
    events: TimeEntriesByDate,
    startDate: Date,
    endDate: Date
): Map<string, number> {
    const weekTotals = new Map<string, number>();
    let currentDate = startDate;

    while ( currentDate <= endDate ) {
        const dateKey = format( currentDate, "yyyy-MM-dd" );
        const timeentries = events[ dateKey ] || [];

        const weekStart = startOfWeek( currentDate, { weekStartsOn: 1 } );
        const weekKey = format( weekStart, "yyyy-MM-dd" );

        const dayTotal = calculateDayTotal( timeentries );
        weekTotals.set( weekKey, ( weekTotals.get( weekKey ) || 0 ) + dayTotal );

        currentDate = addDays( currentDate, 1 );
    }

    return weekTotals;
}

function getMonthDayRange( currentDate: Date ): MonthDayRange {
    const firstDay = startOfMonth( currentDate );
    const lastDay = lastDayOfMonth( currentDate );
    const daysInMonth = getDaysInMonth( currentDate );
    const firstDayWeekDay = getDayMondayBased( firstDay );
    const prevMonthDays = getDaysInMonth( subMonths( currentDate, 1 ) );
    const nextMonthDays = 6 - getDayMondayBased( lastDay );

    return {
        firstDay,
        lastDay,
        daysInMonth,
        firstDayWeekDay,
        prevMonthDays,
        nextMonthDays
    };
}

function createDayInfo(
    date: Date,
    events: TimeEntriesByDate,
    weekTotals: Map<string, number>,
    options: {
        selectedDate?: Date;
        isReadOnly?: boolean;
    } = {}
): DayInfo {
    const dateKey = format( date, "yyyy-MM-dd" );
    const timeentries = events[ dateKey ] || [];
    const dayEvents = mapTimeEntriesToDayEvents( timeentries );
    const todayTotal = calculateDayTotal( timeentries );

    const weekStart = startOfWeek( date, { weekStartsOn: 1 } );
    const weekKey = format( weekStart, "yyyy-MM-dd" );
    const weekTotal = weekTotals.get( weekKey );

    return {
        key: `${ format( date, "yyyy-MM" ) }-${ date.getDate() }`,
        day: date.getDate(),
        newLink: `/app/timetracking/timeentries/create`,
        date,
        dayEvents,
        todayTotal: options.isReadOnly ? undefined : todayTotal,
        weekTotal: options.isReadOnly ? undefined : weekTotal,
        isSunday: getDay( date ) === 0,
        isSelected: options.selectedDate ? isSameDay( date, options.selectedDate ) : false,
        isReadOnly: options.isReadOnly
    };
}


export {
    calculateDayTotal,
    mapTimeEntriesToDayEvents,
    calculateWeekTotals,
    getMonthDayRange,
    createDayInfo
}