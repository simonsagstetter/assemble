/*
 * assemble
 * CalendarHeader.tsx
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */
"use client";
import { addMonths, format, isSameDay, isSameYear, lastDayOfMonth, startOfMonth, subMonths } from "date-fns";
import { ChevronLeft, ChevronRight, Plus } from "lucide-react";
import Link from "next/link";
import useCalendar from "@/hooks/useCalendar";
import { Button } from "@/components/ui/button";
import { ButtonGroup } from "@/components/ui/button-group";
import { Badge } from "@/components/ui/badge";
import {
    getGetOwnTimeEntriesQueryKey,
    getOwnTimeEntries
} from "@/api/rest/generated/query/timeentries/timeentries";
import { useQueryClient } from "@tanstack/react-query";
import { startTransition, useCallback } from "react";
import { toast } from "sonner";
import {
    getGetHolidaysByYearAndSubdivisionCodeQueryKey,
    getHolidaysByYearAndSubdivisionCode
} from "@/api/rest/generated/query/holidays/holidays";


export default function CalendarHeader() {
    const {
        currentDate,
        previousMonth,
        nextMonth,
        today,
        settings,
        selectedDate,
        setEvents,
        setHolidays,
        setCurrentDate,
        setIsLoading,
        isLoading,
        holidays
    } = useCalendar();
    const queryClient = useQueryClient();

    const monthName = format( currentDate, "MMMM" ),
        shortMonthName = format( new Date(), "MMM" ).toUpperCase(),
        year = format( currentDate, "yyyy" ),
        begin = startOfMonth( currentDate ),
        end = lastDayOfMonth( currentDate ),
        selectedDateString = format( selectedDate, "yyyy-MM-dd" );

    const getTimeEntries = useCallback( async ( date: Date ) => {
        const aroundDate = format( date, "yyyy-MM-dd" );
        const timeEntries = await queryClient.fetchQuery( {
            queryKey: getGetOwnTimeEntriesQueryKey( { aroundDate } ),
            queryFn: () => getOwnTimeEntries( { aroundDate } )
        } );
        if ( timeEntries ) {
            const eventData = Object.groupBy(
                timeEntries,
                event => event.date
            );
            setEvents( eventData );
        } else {
            setCurrentDate( currentDate );
            toast.error( "Error", {
                description: "Could not fetch time entries",
            } );
        }
    }, [ queryClient, setEvents, currentDate, setCurrentDate ] );

    const getHolidays = useCallback( async ( year: string ) => {
        const subdivisionCode = "DE-" + settings.subdivisionCode;
        const holidays = await queryClient.fetchQuery( {
            queryKey: getGetHolidaysByYearAndSubdivisionCodeQueryKey( {
                year,
                subdivisionCode
            } ),
            queryFn: () => getHolidaysByYearAndSubdivisionCode( {
                year,
                subdivisionCode
            } )
        } );
        if ( holidays ) {
            const holidayData = Object.groupBy(
                holidays.filter( holiday => holiday.startDate ),
                holiday => holiday.startDate!
            );
            setHolidays( holidayData );
        } else {
            setCurrentDate( currentDate );
            toast.error( "Error", {
                description: "Could not fetch holidays",
            } );
        }
    }, [ settings.subdivisionCode, queryClient, setHolidays, currentDate, setCurrentDate ] )

    const handlePreviousMonth = async () => {
        const prevMonthDate = subMonths( currentDate, 1 );
        previousMonth();
        await getTimeEntries( prevMonthDate );
        if ( !isSameYear( currentDate, prevMonthDate ) ) await getHolidays( format( prevMonthDate, "yyyy" ) );
    }

    const handleToday = async () => {
        const todayDate = new Date();
        today();
        await getTimeEntries( todayDate );
        if ( !isSameYear( currentDate, todayDate ) ) await getHolidays( format( todayDate, "yyyy" ) );
    }


    const handleNextMonth = async () => {
        const nextMonthDate = addMonths( currentDate, 1 );
        nextMonth();
        await getTimeEntries( nextMonthDate );
        if ( !isSameYear( currentDate, nextMonthDate ) ) await getHolidays( format( nextMonthDate, "yyyy" ) );
    }

    const handleMonthChange = async ( dir: "prev" | "today" | "next" = "today" ) => {
        if ( dir === "today" && isSameDay( currentDate, new Date() ) ) return;
        setIsLoading( true );
        startTransition( async () => {
            if ( dir === "prev" ) await handlePreviousMonth();
            else if ( dir === "next" ) await handleNextMonth();
            else await handleToday();
            setIsLoading( false );
        } )
    }


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
                <Button onClick={ () => handleMonthChange( "prev" ) } variant={ "outline" } disabled={ isLoading }>
                    <ChevronLeft size={ 20 } className="text-gray-500"/>
                </Button>
                <Button onClick={ () => handleMonthChange() } variant={ "outline" } disabled={ isLoading }>
                    Today
                </Button>
                <Button onClick={ () => handleMonthChange( "next" ) } variant={ "outline" } disabled={ isLoading }>
                    <ChevronRight size={ 20 } className="text-gray-500"/>
                </Button>
            </ButtonGroup>

            <Button className={ "p-0" } disabled={ holidays[ selectedDateString ] !== undefined }>
                <Link className={ "px-4 py-3 flex flex-row items-center gap-1" }
                      href={ settings.newLink + "?date=" + selectedDateString }><Plus
                    size={ 16 }/> New</Link>
            </Button>
        </div>
        <div className="pointer-events-none absolute bottom-0 left-0 w-full border-t border-gray-200"></div>
    </div>
}