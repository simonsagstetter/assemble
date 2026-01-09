/*
 * assemble
 * useCalendar.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
import { use } from "react";
import { CalendarContext } from "@/store/calendar-store";

export default function useCalendar() {
    const ctx = use( CalendarContext );
    if ( !ctx ) throw new Error( "useCalendar must be used within a <CalendarProvider/> tag" );
    return ctx;
}
