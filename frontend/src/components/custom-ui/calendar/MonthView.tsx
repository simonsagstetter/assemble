/*
 * assemble
 * MonthView.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import CalendarHeader from "@/components/custom-ui/calendar/CalendarHeader";
import MonthHeader from "@/components/custom-ui/calendar/MonthHeader";
import MonthGrid from "@/components/custom-ui/calendar/MonthGrid";
import MonthFooter from "@/components/custom-ui/calendar/MonthFooter";

export default function MonthView() {
    return (
        <div
            className="flex flex-col overflow-hidden rounded-t-none rounded-b-xl bg-background shadow-none ring-1 ring-gray-200 h-full mx-auto w-full">
            <CalendarHeader/>

            <main className="flex flex-1 overflow-hidden">
                <div className="flex flex-1 flex-col">
                    <MonthHeader/>
                    <MonthGrid/>
                    <MonthFooter/>
                </div>
            </main>
        </div>
    );
}