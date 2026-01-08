/*
 * assemble
 * calendar.types.ts
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { type colorClasses } from "@/config/calendar/calendar.config";

type EventData = {
    id: string;
    updateLink: string;
    deleteLink: string;
    isMultiDay: boolean;
    title: string;
    time: number;
    color: ( keyof typeof colorClasses );
    tooltip?: string;
}

type DayInfo = {
    key: string;
    day: number;
    date: Date;
    newLink: string;
    dayEvents: EventData[];
    todayTotal?: number;
    weekTotal?: number;
    isSunday: boolean;
    isSelected?: boolean;
    isReadOnly?: boolean;
}

type MonthDayRange = {
    firstDay: Date;
    lastDay: Date;
    daysInMonth: number;
    firstDayWeekDay: number;
    prevMonthDays: number;
    nextMonthDays: number;
}

export { type EventData, type DayInfo, type MonthDayRange }