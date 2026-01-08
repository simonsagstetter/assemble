/*
 * assemble
 * CalendarHeader.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

import { format, lastDayOfMonth, startOfMonth } from "date-fns";
import { ChevronLeft, ChevronRight, Plus } from "lucide-react";
import Link from "next/link";
import useCalendar from "@/hooks/useCalendar";
import { Button } from "@/components/ui/button";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { ButtonGroup } from "@/components/ui/button-group";
import { Badge } from "@/components/ui/badge";


export default function CalendarHeader() {
    const { currentDate, previousMonth, nextMonth, today, settings, selectedDate, setSettings } = useCalendar();
    const monthName = format( currentDate, "MMMM" ),
        shortMonthName = format( new Date(), "MMM" ).toUpperCase(),
        year = format( currentDate, "yyyy" ),
        begin = startOfMonth( currentDate ),
        end = lastDayOfMonth( currentDate );

    return <div
        className="relative flex flex-col items-center justify-between gap-4 bg-background px-4 py-5 md:flex-row md:px-6 max-md:items-start">
        <div className="flex items-start gap-3">
            <div
                className="inline-flex min-w-16 flex-col items-center overflow-hidden rounded-lg ring-1 ring-gray-200 max-md:hidden">
                <div className="flex w-full justify-center bg-gray-100 px-2 pt-1 pb-0.5">
                    <span className="text-xs font-semibold text-gray-500">{ shortMonthName }</span>
                </div>
                <div className="flex w-full justify-center px-2 pt-px pb-[3px]">
                    <span className="text-lg leading-7 font-bold text-primary">{ format( new Date(), "dd" ) }</span>
                </div>
            </div>
            <div className="flex flex-col gap-0.5">
                <div className="flex items-center gap-2 text-lg font-semibold">
                    { monthName } { year }
                    <Badge variant={ "outline" }>Week { format( begin, "II" ) } - { format( end, "II" ) }</Badge>
                </div>
                <span className="text-sm text-gray-600">
              { format( begin, "do MMM yyyy" ) } – { format( end, "do MMM yyyy" ) }
            </span>
            </div>
        </div>

        <div className="flex flex-wrap items-center gap-3 gap-y-4 max-md:w-full">
            <ButtonGroup className={ "**:shadow-none" }>
                <Button onClick={ previousMonth } variant={ "outline" }>
                    <ChevronLeft size={ 20 } className="text-gray-500"/>
                </Button>
                <Button onClick={ today } variant={ "outline" }>
                    Today
                </Button>
                <Button onClick={ nextMonth } variant={ "outline" }>
                    <ChevronRight size={ 20 } className="text-gray-500"/>
                </Button>
            </ButtonGroup>

            {/*<Select defaultValue={ settings.view } onValueChange={ ( val ) => setSettings( {*/ }
            {/*    ...settings,*/ }
            {/*    view: val as "month" | "week"*/ }
            {/*} ) }>*/ }
            {/*    <SelectTrigger className={ "shadow-none" }>*/ }
            {/*        <SelectValue placeholder="Select View"/>*/ }
            {/*    </SelectTrigger>*/ }
            {/*    <SelectContent>*/ }
            {/*        <SelectItem value="month">Month View</SelectItem>*/ }
            {/*        <SelectItem value="week">Week View</SelectItem>*/ }
            {/*    </SelectContent>*/ }
            {/*</Select>*/ }

            <Button className={ "p-0" }>
                <Link className={ "px-4 py-3 flex flex-row items-center gap-1" }
                      href={ settings.newLink + "?date=" + format( selectedDate, "yyyy-MM-dd" ) }><Plus
                    size={ 16 }/> New</Link>
            </Button>
        </div>
        <div className="pointer-events-none absolute bottom-0 left-0 w-full border-t border-gray-200"></div>
    </div>
}